package com.muhammadali.alarmme.feature.debugPanel.presentation.model

import com.muhammadali.alarmme.mvi.UiEvent

sealed interface DebugPanelEvent : UiEvent {
    data class OnGlobalMocksToggle(val enabled: Boolean) : DebugPanelEvent
    data class OnMockAlarmsToggle(val enabled: Boolean) : DebugPanelEvent
    data class OnMockRingtonesToggle(val enabled: Boolean) : DebugPanelEvent
    data object OnResetAllFlagsClick : DebugPanelEvent

    /** Retry after the debug panel flow failed to load. */
    data object OnRetry : DebugPanelEvent

    // No real destination/integration exists yet for any of these — dispatched so the
    // wiring is in place, but currently no-ops in the ScreenModel.
    data object OnLaunchRingingScreenClick : DebugPanelEvent
    data class OnFireTestAlarmClick(val delayLabel: String) : DebugPanelEvent
    data object OnSeedSampleAlarmsClick : DebugPanelEvent
    data object OnScheduledAlarmsClick : DebugPanelEvent
    data object OnFlushDatabaseClick : DebugPanelEvent
    data object OnClearPreferencesClick : DebugPanelEvent
    data object OnResetOnboardingClick : DebugPanelEvent
}
