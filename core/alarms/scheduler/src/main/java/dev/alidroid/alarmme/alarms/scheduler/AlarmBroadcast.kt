package dev.alidroid.alarmme.alarms.scheduler

import android.content.Intent
import dev.alidroid.alarmme.alarms.domain.AlarmId

/**
 * The contract between the broadcast [AlarmSchedulerImpl] schedules and whoever receives it, and
 * between [AlarmRingingControllerImpl] and whoever rings.
 */
object AlarmBroadcast {
    const val EXTRA_ALARM_ID = "dev.alidroid.alarmme.alarms.scheduler.extra.ALARM_ID"
    const val ACTION_STOP_ALARM = "dev.alidroid.alarmme.alarms.scheduler.action.STOP_ALARM"
}

fun Intent.putAlarmId(alarmId: AlarmId): Intent =
    putExtra(AlarmBroadcast.EXTRA_ALARM_ID, alarmId.value)

fun Intent.alarmIdOrNull(): AlarmId? =
    getStringExtra(AlarmBroadcast.EXTRA_ALARM_ID)?.takeIf { it.isNotBlank() }?.let(::AlarmId)
