package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model

import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.mvi.UiEvent

sealed interface RingtonePickerEvent : UiEvent {
    /** A row was tapped — toggle its preview playback. */
    data class OnRingtoneClick(val id: RingtoneId) : RingtonePickerEvent

    /** A row's radio was tapped — pick it as the alarm tone. */
    data class OnRingtoneSelect(val id: RingtoneId) : RingtonePickerEvent

    /** "Try again" from the error state: start observing the ringtones over. */
    data object OnRetry : RingtonePickerEvent
}
