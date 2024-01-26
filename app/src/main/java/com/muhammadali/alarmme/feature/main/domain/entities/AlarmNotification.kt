package com.muhammadali.alarmme.feature.main.domain.entities

data class AlarmNotification(
    val title: String,
    val alarmTime: String,
    val allowSnooze: Boolean,
    val vibrate: Boolean,
)
