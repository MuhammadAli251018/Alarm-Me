package dev.alidroid.alarmme.feature.alarmRinging.presentation.model

import dev.alidroid.alarmme.mvi.UiState

sealed interface AlarmRingingState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmRingingState

    data class Loaded(val alarm: AlarmRingingUiState) : AlarmRingingState

    data object Error : AlarmRingingState
}
