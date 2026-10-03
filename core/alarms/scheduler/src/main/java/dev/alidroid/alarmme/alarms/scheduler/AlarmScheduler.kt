package dev.alidroid.alarmme.alarms.scheduler

import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.datetime.LocalDateTime

interface AlarmScheduler {
    suspend fun scheduleOrUpdateAlarm(alarmId: AlarmId, time: LocalDateTime)
    suspend fun cancelAlarm(alarmId: AlarmId): Boolean
}