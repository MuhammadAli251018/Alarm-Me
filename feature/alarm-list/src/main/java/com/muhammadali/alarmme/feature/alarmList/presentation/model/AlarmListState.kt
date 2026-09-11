package com.muhammadali.alarmme.feature.alarmList.presentation.model

import com.muhammadali.alarmme.feature.alarmList.presentation.NextAlarmsFireTime
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItem
import com.muhammadali.alarmme.mvi.UiState


sealed interface AlarmsListState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmsListState

    data class Loaded(
        val nextAlarmsFireTime: NextAlarmsFireTime,
        val alarms: List<AlarmItem>
    ) : AlarmsListState

    data object Error : AlarmsListState
}
