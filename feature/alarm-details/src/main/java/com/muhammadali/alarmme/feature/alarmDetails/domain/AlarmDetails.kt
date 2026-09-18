package com.muhammadali.alarmme.feature.alarmDetails.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.DayOfWeek

@JvmInline
value class AlarmDetailsId(val value: String)

data class AlarmDetails(
    val id: AlarmDetailsId,
    val label: String,
    val time: LocalDateTime,
    val repeatDays: Set<DayOfWeek>,
    val customDateEnabled: Boolean,
    val customDates: List<LocalDate>,
    val soundName: String,
    val snoozeDurationMinutes: Int,
    val snoozeCount: Int,
    val gradualVolumeEnabled: Boolean,
    val vibrateEnabled: Boolean,
)
