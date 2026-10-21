package dev.alidroid.alarmme.ringing

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.ServiceCompat
import dev.alidroid.alarmme.AppStartupInitializer
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.getAlarm
import dev.alidroid.alarmme.alarms.scheduler.AlarmBroadcast
import dev.alidroid.alarmme.alarms.scheduler.alarmIdOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.minutes

private val ALARM_STARTUP_WAKE_LOCK_TIMEOUT = 1.minutes

class AlarmRingingService : Service() {

    private val alarmsRepository: AlarmsRepository by inject()

    private val notificationBuilder by lazy { AlarmNotificationBuilder(this) }
    private val vibrator by lazy { AlarmVibrator(this) }

    private val ringingCoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @Volatile
    private var ringingJob: Job? = null

    @Volatile
    private var ringtonePlayer: AlarmRingtonePlayer? = null

    @Volatile
    private var ringingAlarmId: AlarmId? = null

    override fun onCreate() {
        super.onCreate()

        AppStartupInitializer.start()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmId = intent?.alarmIdOrNull()

        when {
            intent?.action == AlarmBroadcast.ACTION_STOP_ALARM -> stopRinging(alarmId)
            alarmId != null -> startRinging(alarmId)
            else -> stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        silence()
        super.onDestroy()
    }

    private fun startRinging(alarmId: AlarmId) {
        if (ringingAlarmId == alarmId) return

        if (!notificationBuilder.canPostAlarmNotification()) {
            stopSelf()
            return
        }

        // In case two alarms where fired at the same time, the later one takes over.
        if (ringingAlarmId != null) silence()

        runWithAlarmWakeLock(this) {
            postRingingNotification(alarmId)
            ringingAlarmId = alarmId
            ringingJob = ringingCoroutineScope.launch { ring(alarmId) }
        }
    }

    private suspend fun ring(alarmId: AlarmId) {
        AppStartupInitializer.isReady.first { it }

        val alarm = alarmsRepository.getAlarm(alarmId)
        if (alarm == null) {
            silenceAndStop()
            return
        }

        if (alarm.preferences.vibrationEnabled) vibrator.startVibration()

        val player = AlarmRingtonePlayer(applicationContext, alarm.preferences.ringtone)
            .apply { startRinging() }

        if (ringingAlarmId == alarmId) {
            ringtonePlayer = player
        } else {
            vibrator.stopVibration()
            withContext(NonCancellable) { player.stopRinging() }
        }
    }


    /** @param alarmId the alarm the stop was meant for, or null for whatever is ringing. */
    private fun stopRinging(alarmId: AlarmId?) {
        val ringing = ringingAlarmId
        if (alarmId != null && ringing != null && alarmId != ringing) return

        silenceAndStop()
    }

    /** Stops the alarm and the service with it. */
    private fun silenceAndStop() {
        val silenced = silence()

        ringingCoroutineScope.launch {
            // The service is what owns the ringtone, so it goes after the ringtone is handed back.
            silenced.join()

            // A start that landed while that was happening is the one ringing now, and stopping
            // the service would silence it.
            if (ringingAlarmId != null) return@launch

            // Nothing else can clear a foreground notification.
            ServiceCompat.stopForeground(
                this@AlarmRingingService,
                ServiceCompat.STOP_FOREGROUND_REMOVE
            )
            stopSelf()
        }
    }

    private fun silence(): Job {
        ringingAlarmId = null
        ringingJob?.cancel()
        ringingJob = null

        vibrator.stopVibration()

        val player = ringtonePlayer
        ringtonePlayer = null

        return ringingCoroutineScope.launch { player?.stopRinging() }
    }

    private fun postRingingNotification(alarmId: AlarmId) {
        ServiceCompat.startForeground(
            this,
            ALARM_NOTIFICATION_ID,
            notificationBuilder.build(alarmId),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
            else 0
        )
    }
}

private inline fun runWithAlarmWakeLock(context: Context, block: () -> Unit) {
    val powerManager = context.getSystemService(PowerManager::class.java) as PowerManager
    val wakeLock =
        powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmRingingService:Wakelock")

    try {
        wakeLock.acquire(ALARM_STARTUP_WAKE_LOCK_TIMEOUT.inWholeMilliseconds)
        block()
    } finally {
        if (wakeLock.isHeld) wakeLock.release()
    }
}
