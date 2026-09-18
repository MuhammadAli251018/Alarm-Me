package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetails
import com.muhammadali.alarmme.mvi.UiState

sealed interface AlarmDetailsState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmDetailsState

    data class Loaded(
        val original: AlarmDetails,
        val draft: AlarmDetails,
    ) : AlarmDetailsState {
        val canSave: Boolean get() = draft != original
    }

    data object Error : AlarmDetailsState
}
