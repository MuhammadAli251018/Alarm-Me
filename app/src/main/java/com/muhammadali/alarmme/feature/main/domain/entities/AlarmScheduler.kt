package com.muhammadali.alarmme.feature.main.domain.entities

interface AlarmScheduler {
    fun scheduleOrUpdate(alarm: Alarm)

    fun cancelAlarm(alarmId: String)
}