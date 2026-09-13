package com.muhammadali.alarmme.feature.alarmList.presentation.model

import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItem
import com.muhammadali.alarmme.mvi.UiState
import kotlinx.datetime.LocalDateTime


sealed interface AlarmsListState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmsListState

    data class Loaded(
        val alarms: List<AlarmItem>,
        val nextAlarmTime: LocalDateTime?
    ) : AlarmsListState

    data object Error : AlarmsListState
}
