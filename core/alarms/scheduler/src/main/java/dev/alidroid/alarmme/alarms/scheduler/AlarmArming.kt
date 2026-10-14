package dev.alidroid.alarmme.alarms.scheduler

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.nextOccurrence
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.LocalDateTime

suspend fun AlarmScheduler.scheduleNextOccurrence(
    alarm: Alarm,
    now: LocalDateTime = LocalDateTime.now(),
): LocalDateTime? {
    val next = alarm.nextOccurrence(now) ?: return null

    scheduleOrUpdateAlarm(alarm.id, next)

    return next
}
