package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmSchedule
import com.muhammadali.alarmme.alarms.domain.Ringtone
import com.muhammadali.alarmme.alarms.domain.SnoozePreferences
import com.muhammadali.alarmme.alarms.domain.nextOccurrence
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration.Companion.minutes

/**
 * The domain → state boundary for alarm details. */
internal fun Alarm.toUiState(now: LocalDateTime): AlarmDetailsUiState = AlarmDetailsUiState(
    id = id,
    label = label,
    time = time,
    nextOccurrence = nextOccurrence(now),
    repeatDays = (schedule as? AlarmSchedule.Repeat)?.repeatDays ?: emptySet(),
    customDateEnabled = schedule is AlarmSchedule.CustomDates,
    customDates = (schedule as? AlarmSchedule.CustomDates)?.customDates ?: emptyList(),
    ringtoneId = preferences.ringtone.id,
    soundName = preferences.ringtone.name,
    snoozeDurationMinutes = preferences.snoozePreferences
        ?.snoozeDuration
        ?.inWholeMinutes
        ?.toInt()
        ?: 0,
    snoozeCount = preferences.snoozePreferences?.snoozeCount ?: 0,
    gradualVolumeEnabled = preferences.gradualVolumeEnabled,
    vibrateEnabled = preferences.vibrationEnabled
)

/**
 * State → domain, for saving alarm details.
 */
internal fun AlarmDetailsUiState.toAlarm(base: Alarm, ringtone: Ringtone): Alarm = base.copy(
    label = label,
    time = time,
    schedule = if (customDateEnabled) {
        AlarmSchedule.CustomDates(customDates)
    } else {
        AlarmSchedule.Repeat(repeatDays)
    },
    preferences = base.preferences.copy(
        ringtone = ringtone,
        snoozePreferences = toSnoozePreferences(),
        gradualVolumeEnabled = gradualVolumeEnabled,
        vibrationEnabled = vibrateEnabled
    )
)

private fun AlarmDetailsUiState.toSnoozePreferences(): SnoozePreferences? =
    if (snoozeDurationMinutes == 0 && snoozeCount == 0) {
        null
    } else {
        SnoozePreferences(snoozeDurationMinutes.minutes, snoozeCount)
    }
