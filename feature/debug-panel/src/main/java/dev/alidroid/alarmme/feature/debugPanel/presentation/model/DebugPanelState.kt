package dev.alidroid.alarmme.feature.debugPanel.presentation.model

import dev.alidroid.alarmme.mvi.UiState

sealed interface DebugPanelState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : DebugPanelState

    data class Loaded(
        val buildInfo: String,
        val globalMocksEnabled: Boolean,
        val mockAlarmsEnabled: Boolean,
        val mockRingtonesEnabled: Boolean,
        val scheduledAlarmsCount: Int,
        val exactAlarmPermissionGranted: Boolean,
    ) : DebugPanelState

    data object Error : DebugPanelState
}
