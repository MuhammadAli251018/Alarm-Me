package com.muhammadali.alarmme.feature.alarmRinging.presentation.model

import com.muhammadali.alarmme.mvi.UiState

sealed interface AlarmRingingState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmRingingState

    data class Loaded(val alarm: AlarmRingingUiState) : AlarmRingingState

    data object Error : AlarmRingingState
}
