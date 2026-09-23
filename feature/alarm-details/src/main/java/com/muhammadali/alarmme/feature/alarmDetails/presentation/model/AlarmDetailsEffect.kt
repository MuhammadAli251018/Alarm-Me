package com.muhammadali.alarmme.feature.alarmDetails.presentation.model

import com.muhammadali.alarmme.mvi.UiEffect

sealed interface AlarmDetailsEffect : UiEffect {

    data object NavigateBack : AlarmDetailsEffect
}
