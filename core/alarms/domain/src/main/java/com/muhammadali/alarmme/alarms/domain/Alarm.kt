package com.muhammadali.alarmme.alarms.domain

import kotlinx.datetime.LocalTime

@JvmInline
value class AlarmId(val value: String)

data class Alarm(
    val id: AlarmId,
    val enabled: Boolean,
    val label: String,
    val time: LocalTime,
    val schedule: AlarmSchedule,
    val preferences: AlarmPreferences
)