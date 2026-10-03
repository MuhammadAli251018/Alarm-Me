package dev.alidroid.alarmme.alarms.scheduler

import android.content.Intent
import dev.alidroid.alarmme.alarms.domain.AlarmId

/** The contract between the broadcast [AlarmSchedulerImpl] schedules and whoever receives it. */
object AlarmBroadcast {
    const val EXTRA_ALARM_ID = "dev.alidroid.alarmme.alarms.scheduler.extra.ALARM_ID"
}

fun Intent.putAlarmId(alarmId: AlarmId): Intent =
    putExtra(AlarmBroadcast.EXTRA_ALARM_ID, alarmId.value)

fun Intent.alarmIdOrNull(): AlarmId? =
    getStringExtra(AlarmBroadcast.EXTRA_ALARM_ID)?.takeIf { it.isNotBlank() }?.let(::AlarmId)
