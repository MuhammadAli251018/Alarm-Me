package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetails
import com.muhammadali.alarmme.mvi.UiState

sealed interface AlarmDetailsState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmDetailsState

    data class Loaded(
        val alarmDetails: AlarmDetails,
        val canSave: Boolean,
    ) : AlarmDetailsState

    data object Error : AlarmDetailsState
}
