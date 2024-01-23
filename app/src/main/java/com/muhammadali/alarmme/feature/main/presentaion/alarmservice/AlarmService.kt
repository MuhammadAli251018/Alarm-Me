package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.common.Notifications
import com.muhammadali.alarmme.feature.main.data.AlarmEntity
import com.muhammadali.alarmme.feature.main.data.repo.DBRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmService @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val alarmNotificationCreator: AlarmNotificationCreator,
    private val dbRepository: DBRepository
        ) : Service() {

    //todo create exception for each case and handle them

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        val wakeLock: PowerManager.WakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmConstants.WAKE_LOCK_TAG)
            }
        //wake lock ensures that the service starts even when the screen in off
        //ensure that the acquired wake lock is released
        wakeLock.release()

        if (intent != null) {
            when (intent.action) {
                AlarmConstants.RECEIVE_ALARM_ACTION -> {
                    fireAlarm(intent)
                }
                AlarmConstants.END_ALARM_ACTION -> {
                    endAlarm(intent)
                }
                AlarmConstants.SNOOZE_ALARM_ACTION -> {
                    snoozeAlarm(intent)
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun fireAlarm(intent: Intent) {
        val time = intent.extras?.getString(AlarmNotificationCreator.alarmTimeKey) ?: throw Exception()
        val title = intent.extras?.getString(AlarmNotificationCreator.alarmTitleKey) ?: throw Exception()
        val sound = intent.extras?.getString(AlarmNotificationCreator.alarmSoundUriKey) ?: throw Exception()
        val vibration = intent.extras?.getString(AlarmNotificationCreator.alarmVibrationKey) ?: throw Exception()
        val snooze = intent.extras?.getBoolean(AlarmNotificationCreator.alarmSnoozeKey) ?: throw Exception()
        val soundUri = Uri.parse(sound) ?: throw Exception()
        val vibrationPattern = getVibrationPattern(vibration)

        alarmNotificationCreator.fireAlarm(
            this@AlarmService,
            Notifications.ALARMS_CHANNEL_ID,
            time,
            title,
            soundUri,
            vibrationPattern,
            snooze
        )
    }

    private fun getVibrationPattern(pattern: String): LongArray {
        if (pattern.length < 60/*todo provide pattern*/)
            throw Exception()


        val vibration = mutableListOf<Long>()

        pattern.forEach { c ->
            val num = c.digitToIntOrNull() ?: throw Exception()
            vibration.add(num.toLong())
        }
        return vibration.toLongArray()
    }

    private fun endAlarm(intent: Intent) {
        val id = intent.extras?.getInt(AlarmNotificationCreator.alarmIdKey) ?: throw Exception()

        alarmNotificationCreator.cancelAlarm()

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch (Dispatchers.IO) {
            var alarmEntity: AlarmEntity

            dbRepository.apply{
                getAlarmById(id).collectLatest {
                    alarmEntity = it
                    deleteAlarm(alarmEntity)
                }
            }

            //todo schedule the next alarm
        }
    }



    private fun snoozeAlarm(intent: Intent) {

        val time = intent.extras?.getLong(AlarmScheduler.ALARM_TIME_KEY) ?: throw Exception()
        val textTime = intent.extras?.getString(AlarmNotificationCreator.alarmTimeKey) ?: throw Exception()
        val title = intent.extras?.getString(AlarmScheduler.ALARM_TITLE_KEY) ?: throw Exception()
        val sound = intent.extras?.getString(AlarmScheduler.ALARM_SOUND_URI_KEY) ?: throw Exception()
        val vibration = intent.extras?.getBoolean(AlarmScheduler.ALARM_VIBRATION_KEY) ?: throw Exception()
        val enableSnooze = intent.extras?.getBoolean(AlarmScheduler.ALARM_VIBRATION_KEY) ?: throw Exception()
        val alarmDBId = intent.extras?.getInt(AlarmScheduler.ALARM_DB_ID_KEY) ?: throw Exception()

        alarmScheduler.setAlarm(
            time = time /*todo add 5 min*/,
            this@AlarmService,
            AlarmReceiver::class.java,
            alarmDBId = alarmDBId,
            alarmTitle = title,
            alarmTime = textTime,
            alarmSoundUri = sound,
            alarmVibration = vibration,
            alarmSnooze = enableSnooze
        )
    }
}