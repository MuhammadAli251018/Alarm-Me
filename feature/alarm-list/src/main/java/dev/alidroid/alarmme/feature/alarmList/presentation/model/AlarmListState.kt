package dev.alidroid.alarmme.feature.alarmList.presentation.model

import dev.alidroid.alarmme.mvi.UiState
import kotlinx.datetime.LocalDateTime


sealed interface AlarmsListState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmsListState

    data class Loaded(
        val alarms: List<AlarmItemState>,
        val nextAlarmTime: LocalDateTime?
    ) : AlarmsListState

    data object Error : AlarmsListState
}
