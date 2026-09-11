package com.muhammadali.alarmme.feature.alarmList.presentation.model

import com.muhammadali.alarmme.mvi.UiEvent

sealed interface AlarmsListEvent : UiEvent {
    data class OnEnableOrDisableAlarm(val id: String, val enabled: Boolean)
    data class OnDeleteAlarms(val ids: List<String>) : AlarmsListEvent
}