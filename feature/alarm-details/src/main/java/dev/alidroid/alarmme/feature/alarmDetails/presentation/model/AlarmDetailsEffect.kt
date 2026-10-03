package dev.alidroid.alarmme.feature.alarmDetails.presentation.model

import dev.alidroid.alarmme.mvi.UiEffect

sealed interface AlarmDetailsEffect : UiEffect {

    data object NavigateBack : AlarmDetailsEffect
}
