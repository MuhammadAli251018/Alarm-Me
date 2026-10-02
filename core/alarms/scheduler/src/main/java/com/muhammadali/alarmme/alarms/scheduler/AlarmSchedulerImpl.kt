package com.muhammadali.alarmme.alarms.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.utility.kotlinDateTime.toEpochMilliseconds
import kotlinx.datetime.LocalDateTime

val AlarmId.hashCode: Int
    get() = value.hashCode()

class AlarmSchedulerImpl(
    private val context: Context,
    private val receiverClazz: Class<out BroadcastReceiver>
) : AlarmScheduler {

    override suspend fun scheduleOrUpdateAlarm(
        alarmId: AlarmId,
        time: LocalDateTime
    ) {
        val pendingIntent = pendingIntentOf(alarmId)

        // AlarmClockInfo wants an absolute wall-clock time, not a delay from now.
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                time.toEpochMilliseconds(),
                pendingIntent
            ),
            pendingIntent
        )
    }

    override suspend fun cancelAlarm(alarmId: AlarmId): Boolean {
        return runCatching {
            val pendingIntent = pendingIntentOf(alarmId)
            alarmManager.cancel(pendingIntent)
            true
        }.getOrNull() ?: false
    }

    private val alarmManager by lazy {
        context.getSystemService(AlarmManager::class.java)
    }

    private fun pendingIntentOf(alarmId: AlarmId): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            alarmId.hashCode,
            Intent(context, receiverClazz).putAlarmId(alarmId),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}