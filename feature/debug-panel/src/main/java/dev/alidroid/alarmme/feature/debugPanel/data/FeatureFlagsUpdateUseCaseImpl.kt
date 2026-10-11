package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository

class FeatureFlagsUpdateUseCaseImpl(
    private val featureFlagsRepository: FeatureFlagsRepository,
) : FeatureFlagsUpdateUseCase {

    override suspend fun setGlobalMocks(enabled: Boolean) =
        featureFlagsRepository.setGlobalMocks(enabled)

    override suspend fun setMockAlarms(enabled: Boolean) =
        featureFlagsRepository.setMockAlarms(enabled)

    override suspend fun setMockRingtones(enabled: Boolean) =
        featureFlagsRepository.setMockRingtones(enabled)

    /** Clears the store so every flag falls back to the default declared in `FeatureFlagsRepositoryImpl`. */
    override suspend fun resetAllFlags() = featureFlagsRepository.clear()
}
