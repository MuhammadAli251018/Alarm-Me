package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.featureFlags.FeatureFlags
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsSnapshot
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
