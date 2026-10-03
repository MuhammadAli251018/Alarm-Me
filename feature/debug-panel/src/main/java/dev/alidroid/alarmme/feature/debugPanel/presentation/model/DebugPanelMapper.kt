package dev.alidroid.alarmme.feature.debugPanel.presentation.model

import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsSnapshot

/**
 * The domain → state boundary for the debug panel screen.
 */
internal fun toLoadedState(
    flags: FeatureFlagsSnapshot,
    scheduledAlarmsCount: Int,
    exactAlarmPermissionGranted: Boolean,
    buildInfo: String,
): DebugPanelState.Loaded = DebugPanelState.Loaded(
    buildInfo = buildInfo,
    globalMocksEnabled = flags.globalMocksEnabled,
    mockAlarmsEnabled = flags.mockAlarmsEnabled,
    mockRingtonesEnabled = flags.mockRingtonesEnabled,
    scheduledAlarmsCount = scheduledAlarmsCount,
    exactAlarmPermissionGranted = exactAlarmPermissionGranted,
)
