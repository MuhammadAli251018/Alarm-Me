package com.muhammadali.alarmme.alarms.domain

import kotlin.time.Duration

data class SnoozePreferences(
    val snoozeDuration: Duration,
    val snoozeCount: Int,
)

data class AlarmPreferences(
    val ringtone: Ringtone,
    val snoozePreferences: SnoozePreferences?,
    val vibrationEnabled: Boolean,
    val gradualVolumeEnabled: Boolean
)