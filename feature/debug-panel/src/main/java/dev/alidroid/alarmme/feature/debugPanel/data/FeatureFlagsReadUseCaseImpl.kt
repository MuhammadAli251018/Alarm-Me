package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsSnapshot
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FeatureFlagsReadUseCaseImpl(
    private val featureFlagsRepository: FeatureFlagsRepository,
) : FeatureFlagsReadUseCase {

    override fun observeFlags(): Flow<FeatureFlagsSnapshot> = combine(
        featureFlagsRepository.observeGlobalMocks(),
        featureFlagsRepository.observeMockAlarms(),
        featureFlagsRepository.observeMockRingtones(),
    ) { globalMocks, mockAlarms, mockRingtones ->
        FeatureFlagsSnapshot(
            globalMocksEnabled = globalMocks,
            mockAlarmsEnabled = mockAlarms,
            mockRingtonesEnabled = mockRingtones,
        )
    }
}
