package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.core.screens.ringtonePicker.presentatoin.Ringtone
import com.muhammadali.alarmme.mvi.UiEvent
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

sealed interface AlarmDetailsEvent : UiEvent {

    /** The time-picker's Save was tapped with this time. */
    data class OnAlarmTimeChanged(val time: LocalDateTime) : AlarmDetailsEvent

    /** A repeat-day chip was tapped — flip its membership in the draft's repeat days. */
    data class OnDayToggle(val day: DayOfWeek) : AlarmDetailsEvent

    /** The "Custom date" switch was flipped. */
    data class OnCustomDateToggle(val enabled: Boolean) : AlarmDetailsEvent

    /** The date-picker's Save was tapped with this selection. */
    data class OnCustomDatesChanged(val dates: Set<LocalDate>) : AlarmDetailsEvent

    /** The Snooze row was tapped. */
    data object OnSnoozeClick : AlarmDetailsEvent

    /** The "Gradual volume" switch was flipped. */
    data class OnGradualVolumeToggle(val enabled: Boolean) : AlarmDetailsEvent

    /** The "Vibrate" switch was flipped. */
    data class OnVibrateToggle(val enabled: Boolean) : AlarmDetailsEvent

    /** The Label row was tapped. */
    data object OnLabelClick : AlarmDetailsEvent

    /** Persist the current draft. */
    data object OnSaveClick : AlarmDetailsEvent

    /** Delete this alarm. */
    data object OnDeleteClick : AlarmDetailsEvent

    /** "Try again" from the error state: start observing this alarm's details over. */
    data object OnRetry : AlarmDetailsEvent

    data class OnRingtoneUpdated(val pickerRingtone: Ringtone) : AlarmDetailsEvent
}
