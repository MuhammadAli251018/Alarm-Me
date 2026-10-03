package dev.alidroid.alarmme.feature.debugPanel.presentation.model

import dev.alidroid.alarmme.mvi.UiEvent
import kotlin.time.Duration

sealed interface DebugPanelEvent : UiEvent {
    data class OnGlobalMocksToggle(val enabled: Boolean) : DebugPanelEvent
    data class OnMockAlarmsToggle(val enabled: Boolean) : DebugPanelEvent
    data class OnMockRingtonesToggle(val enabled: Boolean) : DebugPanelEvent
    data object OnResetAllFlagsClick : DebugPanelEvent

    /** Retry after the debug panel flow failed to load. */
    data object OnRetry : DebugPanelEvent

    data object OnSeedSampleAlarmsClick : DebugPanelEvent

    // Danger zone.
    data object OnFlushDatabaseClick : DebugPanelEvent
    data object OnClearPreferencesClick : DebugPanelEvent
    data object OnResetOnboardingClick : DebugPanelEvent

    // No real destination/integration exists yet for any of these — dispatched so the
    // wiring is in place, but currently no-ops in the ScreenModel.
    data object OnLaunchRingingScreenClick : DebugPanelEvent
    data class OnFireTestAlarmClick(val duration: Duration) : DebugPanelEvent
    data object OnScheduledAlarmsClick : DebugPanelEvent
}
