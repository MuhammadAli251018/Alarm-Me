package com.muhammadali.alarmme.feature.allAlarms.presentation.models

import com.muhammadali.alarmme.common.presentation.UiEffect

sealed class AllAlarmsEffect : UiEffect {
    data object NavigateToNewAlarmEffect : AllAlarmsEffect()
    data class NavigateToEditAlarmEffect(val index: Int) : AllAlarmsEffect()
}