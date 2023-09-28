package com.muhammadali.alarmme.feature.main.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.ui.util.toAnnotatedString
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

class AlarmSchedulerImp @Inject constructor(
    private val alarmManager: AlarmManager,
    private val timeAdapter: TimeAdapter
) : AlarmScheduler {
    override val scheduler = ScheduleAlarm { alarm: Alarm, context: Context ->
        val receiver = AlarmReceiver::class.java
        val time = timeAdapter.getTimeFormat(alarm.time)
        val textTime = time.toAnnotatedString().text
        val snooze = alarm.snooze.toBooleanStrict()

        setAlarm(
            time = alarm.time,
            context = context,
            receiver = receiver,
            alarmDBId = alarm.id,
            alarmTitle = alarm.title,
            alarmSoundUri = alarm.ringtoneRef,
            alarmTime = textTime,
            alarmSnooze = snooze,
            alarmVibration = alarm.vibration
        )
    }
    override fun setAlarm(
        time: Long,
        context: Context,
        receiver: Class<out BroadcastReceiver>,
        alarmDBId: Int,
        alarmTitle: String,
        alarmTime: String,
        alarmSoundUri: String,
        alarmVibration: Boolean,
        alarmSnooze: Boolean,
        alarmId: Int
    ) {
        val intent = Intent(context, receiver).apply {
            action = AlarmConstants.RECEIVE_ALARM_ACTION
            putExtra("alarmId", alarmDBId)
            putExtra("alarmTitle", alarmTitle)
            putExtra("alarmTime", alarmTime)
            putExtra("alarmSoundUri", alarmSoundUri)
            putExtra("alarmVibration", alarmVibration)
            putExtra("alarmSnooze", alarmSnooze)
        }

        val pendingIntent = PendingIntent.getBroadcast(context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        //todo log for testing
        val is31OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        Log.d("AlarmScheduler", "is31OrAbove: $is31OrAbove")

        if (is31OrAbove) {
            //todo log for testing
            val canSchedule = alarmManager.canScheduleExactAlarms()
            Log.d("AlarmScheduler", "canSchedule: $canSchedule")

            if (canSchedule)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            else {
            }
        }    // handle
        else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
            //handle
    }

    override fun cancelAlarm( context: Context, receiver: Class<out BroadcastReceiver>, alarmId: Int) {
        val intent = Intent(context, receiver)
        val pendingIntent = PendingIntent.getBroadcast(context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }

}