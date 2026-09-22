package com.muhammadali.alarmme.core.screens.ringtonePicker.presentatoin.model

import com.muhammadali.alarmme.mvi.UiEvent

sealed interface RingtonePickerEvent : UiEvent {
    /** A row was tapped — toggle its preview playback. */
    data class OnRingtoneClick(val id: String) : RingtonePickerEvent

    /** A row's radio was tapped — pick it as the alarm tone. */
    data class OnRingtoneSelect(val id: String) : RingtonePickerEvent

    /** "Try again" from the error state: start observing the ringtones over. */
    data object OnRetry : RingtonePickerEvent
}
