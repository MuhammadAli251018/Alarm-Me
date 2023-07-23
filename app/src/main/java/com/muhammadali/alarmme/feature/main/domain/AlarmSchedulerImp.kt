package com.muhammadali.alarmme.feature.main.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import javax.inject.Inject

class AlarmSchedulerImp @Inject constructor(
    private val alarmManager: AlarmManager
) : AlarmScheduler {
    override fun setAlarm(time: Long, context: Context, receiver: Class<out BroadcastReceiver>, alarmId: Int) {
        val intent = Intent(context, receiver)
        val pendingIntent = PendingIntent.getBroadcast(context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
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