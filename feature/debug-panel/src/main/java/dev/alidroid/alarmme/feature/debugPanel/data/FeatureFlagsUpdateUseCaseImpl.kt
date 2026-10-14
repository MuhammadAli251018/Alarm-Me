package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository

class FeatureFlagsUpdateUseCaseImpl(
    private val featureFlagsRepository: FeatureFlagsRepository,
) : FeatureFlagsUpdateUseCase {

    /**
     * Switching the master off also clears the flags it governs. The DI wiring reads the individual
     * flags rather than this one, so a stored `true` must not be allowed to outlive the switch that
     * was gating it — otherwise the panel shows a mock as off while the app still uses it.
     */
    override suspend fun setGlobalMocks(enabled: Boolean) {
        featureFlagsRepository.setGlobalMocks(enabled)

        if (!enabled) {
            featureFlagsRepository.setMockAlarms(false)
            featureFlagsRepository.setMockRingtones(false)
        }
    }

    override suspend fun setMockAlarms(enabled: Boolean) =
        featureFlagsRepository.setMockAlarms(enabled)

    override suspend fun setMockRingtones(enabled: Boolean) =
        featureFlagsRepository.setMockRingtones(enabled)

    /** Clears the store so every flag falls back to the default declared in `FeatureFlagsRepositoryImpl`. */
    override suspend fun resetAllFlags() = featureFlagsRepository.clear()
}
