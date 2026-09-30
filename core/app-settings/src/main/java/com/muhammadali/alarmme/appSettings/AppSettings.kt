package com.muhammadali.alarmme.appSettings

import com.muhammadali.alarmme.preferences.PrefRepositoryKey
import com.muhammadali.alarmme.preferences.PreferencesKey
import com.muhammadali.alarmme.preferences.PreferencesManager
import com.muhammadali.alarmme.preferences.PreferencesValue
import com.muhammadali.alarmme.preferences.getPrefValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

interface AppSettingsValue <T> {
    val preferences: PreferencesValue<T>
}

val <T> AppSettingsValue<T>.key: PreferencesKey
    get() = preferences.key

sealed interface AppSettings : KoinComponent {

    companion object {
        val AppSettingsPrefRepoKey = PrefRepositoryKey("app_settings")

        private inline fun <reified T> KoinComponent.injectPrefValue(key: String, defaultValue: T): Lazy<PreferencesValue<T>> {
            return lazy {
                val preferencesManager: PreferencesManager by inject()
                val repo = preferencesManager.getPreferenceOf(AppSettingsPrefRepoKey)
                repo.getPrefValue(PreferencesKey(key), defaultValue)
            }
        }
    }

    object Ringtone : AppSettings, AppSettingsValue<RingtonePreferences> {
        private const val KEY = "ringtone"

        override val preferences: PreferencesValue<RingtonePreferences> by injectPrefValue(KEY, RingtonePreferences.Default)
    }

    object Snooze : AppSettings, AppSettingsValue<SnoozePreferences> {
        private const val KEY = "snooze"

        override val preferences: PreferencesValue<SnoozePreferences> by injectPrefValue(KEY, SnoozePreferences.NoSnooze)
    }

    object GradualVolume : AppSettings, AppSettingsValue<GradualVolumePreferences> {
        private const val KEY = "gradual_volume"

        override val preferences: PreferencesValue<GradualVolumePreferences> by injectPrefValue(KEY, GradualVolumePreferences.Disabled)
    }

    object Vibration : AppSettings, AppSettingsValue<VibrationPreferences> {
        private const val KEY = "vibration"

        override val preferences: PreferencesValue<VibrationPreferences> by injectPrefValue(KEY, VibrationPreferences.Disabled)
    }

    object AlarmTimeout : AppSettings, AppSettingsValue<AlarmTimeoutPreferences> {
        private const val KEY = "alarm_timeout"

        override val preferences: PreferencesValue<AlarmTimeoutPreferences> by
            injectPrefValue(KEY, AlarmTimeoutPreferences.NoTimeout)
    }

    object VolumeButtonsAction : AppSettings, AppSettingsValue<VolumeButtonsActionPreferences> {
        private const val KEY = "volume_buttons_action"

        override val preferences: PreferencesValue<VolumeButtonsActionPreferences> by
            injectPrefValue(KEY, VolumeButtonsActionPreferences.Nothing)
    }

    object TwentyFourHourTime  : AppSettings, AppSettingsValue<TwentyFourHourTimePreferences> {
        private const val KEY = "twenty_four_hour_time"

        override val preferences: PreferencesValue<TwentyFourHourTimePreferences> by
            injectPrefValue(KEY, TwentyFourHourTimePreferences.Disabled)
    }

    object ThemePreference : AppSettings, AppSettingsValue<ThemePreferencePreferences> {
        private const val KEY = "theme_preference"

        override val preferences: PreferencesValue<ThemePreferencePreferences> by
            injectPrefValue(KEY, ThemePreferencePreferences.System)
    }
}