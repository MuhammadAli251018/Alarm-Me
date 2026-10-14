package dev.alidroid.alarmme.featureFlags

import dev.alidroid.alarmme.preferences.PrefRepositoryKey
import dev.alidroid.alarmme.preferences.PreferencesKey
import dev.alidroid.alarmme.preferences.PreferencesManager
import dev.alidroid.alarmme.preferences.PreferencesValue
import dev.alidroid.alarmme.preferences.getPrefValue
import dev.alidroid.alarmme.preferences.setPrefValue

private val FeatureFlagsPrefRepoKey = PrefRepositoryKey("feature_flags")

class FeatureFlagsRepositoryImpl(preferencesManager: PreferencesManager) : FeatureFlagsRepository {

    private val preferences = preferencesManager.getPreferenceOf(FeatureFlagsPrefRepoKey)

    private val globalMocks = preferences.getPrefValue(PreferencesKey("global_mocks_feature_flag"), false)
    private val mockAlarms = preferences.getPrefValue(PreferencesKey("mock_alarms_feature_flag"), false)
    private val mockRingtones = preferences.getPrefValue(PreferencesKey("mock_ringtones_feature_flag"), false)

    override fun observeGlobalMocks(): PreferencesValue<Boolean> = globalMocks
    override suspend fun setGlobalMocks(enabled: Boolean) = preferences.setPrefValue(globalMocks.key, enabled)

    override fun observeMockAlarms(): PreferencesValue<Boolean> = mockAlarms
    override suspend fun setMockAlarms(enabled: Boolean) = preferences.setPrefValue(mockAlarms.key, enabled)

    override fun observeMockRingtones(): PreferencesValue<Boolean> = mockRingtones
    override suspend fun setMockRingtones(enabled: Boolean) = preferences.setPrefValue(mockRingtones.key, enabled)

    override suspend fun clear() = preferences.clear()
}
