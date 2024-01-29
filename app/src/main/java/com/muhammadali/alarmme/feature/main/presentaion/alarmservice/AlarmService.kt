package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import kotlinx.coroutines.runBlocking
import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDB
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepoImp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class AlarmService : Service() {


    private lateinit var alarmScheduler: AlarmScheduler
    private val timeAdapter: TimeAdapter = TimeAdapterImp()
    private lateinit var alarmNotificator: AlarmNotificatorImp

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

    private fun getAlarm(intent: Intent): Result<Alarm> {
        val alarmEncoded = intent.extras?.getString(AlarmSchedulerImp.AlARM_ID_KEY) ?: return Result.failure(Exception("couldn't find alarm in received intent"))
        return try {
            val alarm = Json.decodeFromString<Alarm>(alarmEncoded)
            Result.success(alarm)
        } catch (e: IllegalArgumentException) {
            Result.failure(Exception("couldn't decode received alarm"))
        }
    }
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        // release the wake lock to ensure the starting of the service
        //wake lock ensures that the service starts even when the screen in off
        //ensure that the acquired wake lock is released

            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmNotificatorImp.WAKE_LOCK_TAG).apply {
                    Toast.makeText(this@AlarmService, "is held: ${isHeld}", Toast.LENGTH_LONG).show()
                    if (isHeld)
                        release()
                }
            }


        alarmNotificator = AlarmNotificatorImp(this)
        alarmScheduler = AlarmSchedulerImp(AlarmReceiver::class.java, this)

        if (intent != null) {

            Log.d("LogAlarm", intent.getStringExtra(AlarmSchedulerImp.AlARM_ID_KEY).toString())
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
        val id = intent.extras?.getInt(AlarmNotificatorImp.alarmIdKey) ?: throw Exception("couldn't find alarm id")

        alarmNotificator.cancelAlarm(id)
    }

    private fun snoozeAlarm(intent: Intent) {

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