package com.muhammadali.alarmme.featureFlags

import com.muhammadali.alarmme.preferences.PrefRepositoryKey
import com.muhammadali.alarmme.preferences.PreferencesKey
import com.muhammadali.alarmme.preferences.PreferencesManager
import com.muhammadali.alarmme.preferences.PreferencesValue
import com.muhammadali.alarmme.preferences.getPrefValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

interface FeatureFlagValue<T> {
    val preferences: PreferencesValue<T>
}

enum class DebugPanelFeatureFlag { Enable, Disable }

sealed interface FeatureFlags : KoinComponent {

    companion object {
        val FeatureFlagsPrefRepoKey = PrefRepositoryKey("feature_flags")

        private fun KoinComponent.injectDebugPanel(): Lazy<DebugPanelFeatureFlag> =
            inject<DebugPanelFeatureFlag>()

        private inline fun <reified T> KoinComponent.injectPrefValue(key: String, defaultValue: T): Lazy<PreferencesValue<T>> {
            return lazy {
                val preferencesManager: PreferencesManager by inject()
                val repo = preferencesManager.getPreferenceOf(FeatureFlagsPrefRepoKey)
                repo.getPrefValue(PreferencesKey(key), defaultValue)
            }
        }
    }

    // Using an enum for type-safety
    object DebugPanel : FeatureFlags {
        val value: DebugPanelFeatureFlag by injectDebugPanel()
    }

    object GlobalMocks : FeatureFlags, FeatureFlagValue<Boolean> {
        private const val KEY = "global_mocks_feature_flag"

        override val preferences: PreferencesValue<Boolean> by injectPrefValue(KEY, false)
    }

    object MockAlarms : FeatureFlags, FeatureFlagValue<Boolean> {
        private const val KEY = "mock_alarms_feature_flag"

        override val preferences: PreferencesValue<Boolean> by injectPrefValue(KEY, false)
    }

    object MockRingtones : FeatureFlags, FeatureFlagValue<Boolean> {
        private const val KEY = "mock_ringtones_feature_flag"

        override val preferences: PreferencesValue<Boolean> by injectPrefValue(KEY, false)
    }
}
