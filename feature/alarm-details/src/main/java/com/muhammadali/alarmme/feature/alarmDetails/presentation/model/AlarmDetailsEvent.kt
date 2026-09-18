package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.mvi.UiEvent
import kotlinx.datetime.DayOfWeek

sealed interface AlarmDetailsEvent : UiEvent {

    /** The alarm time was tapped. */
    data object OnAlarmTimeClick : AlarmDetailsEvent

    /** A repeat-day chip was tapped — flip its membership in the draft's repeat days. */
    data class OnDayToggle(val day: DayOfWeek) : AlarmDetailsEvent

    /** The "Custom date" switch was flipped. */
    data class OnCustomDateToggle(val enabled: Boolean) : AlarmDetailsEvent

    /** The custom-date row was tapped. */
    data object OnCustomDateRowClick : AlarmDetailsEvent

    /** The Sound row was tapped. */
    data object OnSoundClick : AlarmDetailsEvent

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
}
