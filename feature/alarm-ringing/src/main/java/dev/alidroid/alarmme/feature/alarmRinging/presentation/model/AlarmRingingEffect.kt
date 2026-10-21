package dev.alidroid.alarmme.feature.alarmRinging.presentation.model

import dev.alidroid.alarmme.mvi.UiEffect

sealed interface AlarmRingingEffect : UiEffect {

    /** The alarm is silenced and the next occurrence armed: the host closes. */
    data object Dismissed : AlarmRingingEffect
}
