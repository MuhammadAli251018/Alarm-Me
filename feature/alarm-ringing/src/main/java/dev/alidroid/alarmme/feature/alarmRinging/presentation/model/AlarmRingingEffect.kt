package dev.alidroid.alarmme.feature.alarmRinging.presentation.model

import dev.alidroid.alarmme.mvi.UiEffect

sealed interface AlarmRingingEffect : UiEffect {

    /** The alarm is finished with: the host clears its notification and closes. */
    data object Dismissed : AlarmRingingEffect
}
