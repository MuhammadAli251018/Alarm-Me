package com.muhammadali.alarmme.feature.debugPanel.domain

data class FeatureFlagsSnapshot(
    val globalMocksEnabled: Boolean,
    val mockAlarmsEnabled: Boolean,
    val mockRingtonesEnabled: Boolean,
)
