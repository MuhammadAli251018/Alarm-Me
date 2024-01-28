package com.muhammadali.alarmme.feature.main.domain.entities

data class AlarmNotification(
    val id: Int,
    val title: String,
    val time: String,
    val allowSnooze: Boolean,
    val vibrate: LongArray? = longArrayOf(0, 500, 500),
    val ringtoneRef: String
)
