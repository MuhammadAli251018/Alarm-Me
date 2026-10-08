package dev.alidroid.alarmme.feature.alarmDetails.presentation.model

import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.Ringtone
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
    val ringtone: Ringtone,
    val snoozeLengthIndex: Int?,
    val snoozeLimitIndex: Int?,
    val gradualVolumeEnabled: Boolean,
    val vibrateEnabled: Boolean,
)
