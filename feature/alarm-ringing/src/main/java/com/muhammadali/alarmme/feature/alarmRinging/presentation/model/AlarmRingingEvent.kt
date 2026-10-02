package com.muhammadali.alarmme.feature.alarmRinging.presentation.model

import com.muhammadali.alarmme.mvi.UiEvent

sealed interface AlarmRingingEvent : UiEvent {

    /** Retry after the ringing alarm failed to load. */
    data object OnRetry : AlarmRingingEvent

    // Nothing rings or snoozes yet — dispatched so the wiring is in place, but currently
    // no-ops in the ScreenModel.
    data object OnSnoozeClick : AlarmRingingEvent
    data object OnDismiss : AlarmRingingEvent
}
