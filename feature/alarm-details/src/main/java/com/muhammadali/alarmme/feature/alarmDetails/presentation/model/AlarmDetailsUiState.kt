package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.alarms.domain.RingtoneId
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class AlarmDetailsUiState(
    val id: AlarmId,
    val label: String,
    val time: LocalTime,
    val nextOccurrence: LocalDateTime?,
    val repeatDays: Set<DayOfWeek>,
    val customDateEnabled: Boolean,
    val customDates: List<LocalDate>,
    val ringtoneId: RingtoneId,
    val soundName: String,
    val snoozeDurationMinutes: Int,
    val snoozeCount: Int,
    val gradualVolumeEnabled: Boolean,
    val vibrateEnabled: Boolean,
)
