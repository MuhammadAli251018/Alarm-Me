package com.muhammadali.alarmme.alarms.scheduler

import com.muhammadali.alarmme.alarms.domain.AlarmId
import kotlinx.datetime.LocalDateTime

class AlarmSchedulerMockImpl : AlarmScheduler {

    private val alarms = mutableMapOf<AlarmId, LocalDateTime>()

    override suspend fun scheduleOrUpdateAlarm(alarmId: AlarmId, time: LocalDateTime) {
        alarms += alarmId to time
        //TODO("Not yet implemented")
    }

    override suspend fun cancelAlarm(alarmId: AlarmId): Boolean {
        //TODO("Not yet implemented")
        return alarms.remove(alarmId) != null
    }
}