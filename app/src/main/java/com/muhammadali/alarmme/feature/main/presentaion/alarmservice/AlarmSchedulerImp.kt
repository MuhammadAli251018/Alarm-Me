package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmNotificatorImp.Companion.RECEIVE_ALARM_ACTION
import kotlinx.serialization.json.Json

class AlarmSchedulerImp(
    private val receiver: Class< out BroadcastReceiver>,
    private val context: Context
) : AlarmScheduler {
    companion object {
        const val AlARM_ID_KEY = "alarm_Id"
        /*const val ALARM_TIME_KEY = "ALARM_TIME_KEY"
        const val ALARM_TITLE_KEY = "ALARM_TITLE_KEY"
        const val ALARM_DB_ID_KEY = "ALARM_DB_ID_KEY"
        const val ALARM_SOUND_URI_KEY = "ALARM_SOUND_URI_KEY"
        const val ALARM_VIBRATION_KEY = "ALARM_VIBRATION_KEY"
        const val ALARM_SNOOZE_KEY = "ALARM_SNOOZE_KEY"*/
    }

    private fun <T> executeWithContext(operation: (context: Context) -> T): T = operation(context)


    override fun scheduleOrUpdate(alarm: Alarm): Result<Unit> = executeWithContext { context ->
        val intent = Intent(context, receiver).apply {
            action = RECEIVE_ALARM_ACTION
            val encodedAlarm = Json.encodeToString(alarm)
            Log.d("LogAlarm", encodedAlarm)
            putExtra(AlARM_ID_KEY, encodedAlarm)
        }

        val pendingIntent = PendingIntent.getBroadcast(context,
            alarm.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager =context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val is31OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        if (is31OrAbove) {
            val canSchedule = alarmManager.canScheduleExactAlarms()

            if (canSchedule)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.time, pendingIntent)
            else {
                return@executeWithContext Result.failure(Exception("Need Exact alarm permission"))
            }
        }
        else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.time, pendingIntent)
        }

        Result.success(Unit)
    }

    override fun cancelAlarm(alarmId: Int): Result<Unit> = executeWithContext {context ->
        val alarmingIntent = Intent(context, receiver)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmingIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Result.success(alarmManager.cancel(pendingIntent))
    }
}