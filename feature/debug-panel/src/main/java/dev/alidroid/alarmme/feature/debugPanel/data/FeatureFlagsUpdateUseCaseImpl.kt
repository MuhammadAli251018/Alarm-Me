package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.featureFlags.FeatureFlags
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import dev.alidroid.alarmme.preferences.PreferencesRepository
import dev.alidroid.alarmme.preferences.setPrefValue

class FeatureFlagsUpdateUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : FeatureFlagsUpdateUseCase {

    override suspend fun setGlobalMocks(enabled: Boolean) =
        preferencesRepository.setPrefValue(FeatureFlags.GlobalMocks.preferences.key, enabled)

    override suspend fun setMockAlarms(enabled: Boolean) =
        preferencesRepository.setPrefValue(FeatureFlags.MockAlarms.preferences.key, enabled)

    override suspend fun setMockRingtones(enabled: Boolean) =
        preferencesRepository.setPrefValue(FeatureFlags.MockRingtones.preferences.key, enabled)

    /** Clears the store so every flag falls back to the default declared in `FeatureFlags`. */
    override suspend fun resetAllFlags() = preferencesRepository.clear()
}
