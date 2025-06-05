package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import kotlinx.coroutines.runBlocking
import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotificator
import kotlinx.serialization.json.Json


/** this service is started for three reasons,
 * To start alarm notification,
 * To snooze alarm,
 * Or to cancel the alarm
    */

class AlarmService : Service() {
    private val timeAdapter: TimeAdapter = TimeAdapterImp()
    private lateinit var alarmNotificator: AlarmNotificator
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

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
                    if (isHeld)
                        release()
                }
            }

        if (intent != null) {

            alarmNotificator = AlarmNotificatorImp(this)


            Log.d("LogAlarm", intent.getStringExtra(AlarmSchedulerImp.AlARM_ID_KEY).toString())
            when (intent.action) {
                AlarmNotificatorImp.RECEIVE_ALARM_ACTION -> {
                    runBlocking {
                        val alarmNotification = getAlarm(intent).getOrThrow()

                        fireAlarm(alarmNotification.toAlarmNotification(timeAdapter))
                    }
                }
                AlarmNotificatorImp.END_ALARM_ACTION -> {
                    endAlarm(intent)
                }
                AlarmNotificatorImp.SNOOZE_ALARM_ACTION -> {
                    snoozeAlarm(intent)
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
           /* Todo
           val alarm = getAlarm(intent).getOrThrow()
            alarmScheduler.scheduleOrUpdate(
                alarm.copy(
                    time = alarm.time + (alarm.preferences.snooze.duration.inWholeMilliseconds)
                )
            )*/
        }
    }
}