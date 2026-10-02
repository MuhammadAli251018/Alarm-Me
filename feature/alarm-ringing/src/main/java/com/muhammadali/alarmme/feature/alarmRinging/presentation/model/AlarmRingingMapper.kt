package com.muhammadali.alarmme.feature.alarmRinging.presentation.model

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.SnoozePreferences
import kotlinx.datetime.LocalDate

/**
 * The domain → state boundary for the ringing screen. [date] is the day the alarm is ringing
 * on, which the alarm itself doesn't carry.
 */
internal fun Alarm.toRingingUiState(date: LocalDate): AlarmRingingUiState = AlarmRingingUiState(
    id = id,
    time = time,
    label = label,
    date = date,
    phase = AlarmRingingUiState.Phase.Ringing,
    snooze = preferences.snoozePreferences.toSnooze(),
)

/** No snooze configured reads the same on screen as none left: the button is unavailable. */
private fun SnoozePreferences?.toSnooze(): AlarmRingingUiState.Snooze =
    if (this == null || snoozeCount <= 0) {
        AlarmRingingUiState.Snooze.Exhausted
    } else {
        AlarmRingingUiState.Snooze.Available(
            duration = snoozeDuration,
            remainingCount = snoozeCount,
        )
    }
