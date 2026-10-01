package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.featureFlags.FeatureFlags
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FeatureFlagsReadUseCaseImpl : FeatureFlagsReadUseCase {

    override fun observeFlags(): Flow<FeatureFlagsSnapshot> = combine(
        FeatureFlags.GlobalMocks.preferences,
        FeatureFlags.MockAlarms.preferences,
        FeatureFlags.MockRingtones.preferences,
    ) { globalMocks, mockAlarms, mockRingtones ->
        FeatureFlagsSnapshot(
            globalMocksEnabled = globalMocks,
            mockAlarmsEnabled = mockAlarms,
            mockRingtonesEnabled = mockRingtones,
        )
    }
}
