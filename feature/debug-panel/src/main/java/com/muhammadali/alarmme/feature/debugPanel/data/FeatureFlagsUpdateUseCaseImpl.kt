package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.featureFlags.FeatureFlags
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import com.muhammadali.alarmme.preferences.PreferencesRepository
import com.muhammadali.alarmme.preferences.setPrefValue

class FeatureFlagsUpdateUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : FeatureFlagsUpdateUseCase {

    override suspend fun setGlobalMocks(enabled: Boolean) =
        preferencesRepository.setPrefValue(FeatureFlags.GlobalMocks.preferences.key, enabled)

    override suspend fun setMockAlarms(enabled: Boolean) =
        preferencesRepository.setPrefValue(FeatureFlags.MockAlarms.preferences.key, enabled)

    override suspend fun setMockRingtones(enabled: Boolean) =
        preferencesRepository.setPrefValue(FeatureFlags.MockRingtones.preferences.key, enabled)

    override suspend fun resetAllFlags() {
        // TODO:
        setGlobalMocks(false)
        setMockAlarms(false)
        setMockRingtones(false)
    }
}
