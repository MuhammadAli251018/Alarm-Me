package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.widget.Toast
import androidx.room.Room
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDB
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepoImp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmService /*@Inject constructor(
    private val dbRepository: AlarmsDBRepo
)*/ : Service() {

    private lateinit var dbRepository: AlarmsDBRepo

    private val alarmScheduler: AlarmScheduler = AlarmSchedulerImp(AlarmReceiver::class.java) {
        getContext()
    }
    private val timeAdapter: TimeAdapter = TimeAdapterImp()
    private val alarmNotificator = AlarmNotificatorImp {
        getContext()
    }

    //todo create exception for each case and handle them

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getContext() = this

    /* this service is started for three reasons
        1- to start alarm notification
        2- to snooze alarm
        3- to cancel the alarm
    */
    private suspend fun CoroutineScope.getAlarm(intent: Intent): Result<Alarm> {
        return async {
            val alarmId = intent.getIntExtra(AlarmNotificatorImp.alarmIdKey, -1)
            if (alarmId == -1)
            // todo handle
                throw Exception("couldn't find alarmId ")

            dbRepository.getAlarmWithId(alarmId).first()
        }.await()
    }
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        // release the wake lock to ensure the starting of the service
        val wakeLock: PowerManager.WakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmNotificatorImp.WAKE_LOCK_TAG)
            }
        //wake lock ensures that the service starts even when the screen in off
        //ensure that the acquired wake lock is released
        if (wakeLock.isHeld)
            wakeLock.release()

        dbRepository = AlarmsDbRepoImp(Room.databaseBuilder(
            applicationContext,
            AlarmsDB::class.java,
            name = "alarms_db"
        ).build().alarmsDao())

        if (intent != null) {
            val id = intent.getIntExtra("alarmId", -1)
            Toast.makeText(this, "Bla from service id is: ${id}", Toast.LENGTH_LONG).show()

            when (intent.action) {
                AlarmNotificatorImp.RECEIVE_ALARM_ACTION -> {
                    runBlocking {
                        val alarmNotification = getAlarm(intent).getOrThrow()

                        Toast.makeText(this@AlarmService, "alarm: $alarmNotification", Toast.LENGTH_LONG).show()

                        fireAlarm(alarmNotification.toAlarmNotification(timeAdapter))
                    }
                    Toast.makeText(this, "Bla from service option: 1", Toast.LENGTH_LONG).show()

                }
                AlarmNotificatorImp.END_ALARM_ACTION -> {
                    endAlarm(intent)
                    Toast.makeText(this, "Bla from service option: 2", Toast.LENGTH_LONG).show()

                }
                AlarmNotificatorImp.SNOOZE_ALARM_ACTION -> {
                    snoozeAlarm(intent)
                    Toast.makeText(this, "Bla from service option: 3", Toast.LENGTH_LONG).show()

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun fireAlarm(alarmNotification: AlarmNotification) {
        alarmNotificator.fireAlarm(
            alarmNotification
        )
    }

    private fun endAlarm(intent: Intent) {
        val id = intent.extras?.getInt(AlarmNotificatorImp.alarmIdKey) ?: throw Exception()

        alarmNotificator.cancelAlarm(id)
    }

    private fun snoozeAlarm(intent: Intent) {

        // should reschedule alarms

        // todo check to see if a snoozed alarm reached his limit
        runBlocking {
            val alarm = getAlarm(intent).getOrThrow()
            alarmScheduler.scheduleOrUpdate(
                alarm.copy(
                    time = alarm.time + (alarm.preferences.snooze.duration.inWholeMilliseconds)
                )
            )
        }
    }
}