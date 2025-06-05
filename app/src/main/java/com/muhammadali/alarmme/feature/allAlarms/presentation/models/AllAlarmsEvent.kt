package com.muhammadali.alarmme.feature.allAlarms.presentation.models

import com.onemorenerd.core.presentation.UiEvent

// Todo: Add all branches
sealed class AllAlarmsEvent : UiEvent {
    data class AlarmClickedEvent(val index: Int) : AllAlarmsEvent()
    data class AlarmEnableOrDisableEvent(val index: Int) : AllAlarmsEvent()
    data object AddAlarmEvent : AllAlarmsEvent()
}