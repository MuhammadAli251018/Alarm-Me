package dev.alidroid.alarmme.appSettings

import dev.alidroid.alarmme.appSettings.AppSettingsRepository.Companion.DefaultTimeoutDuration
import dev.alidroid.alarmme.preferences.PreferencesValue
import dev.alidroid.alarmme.preferences.awaitFirstRealValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface AppSettingsRepository {

    companion object {
        val DefaultTimeoutDuration = 5.minutes
    }

    fun observeRingtone(): PreferencesValue<RingtonePreferences>
    suspend fun setRingtone(value: RingtonePreferences)

    fun observeSnooze(): PreferencesValue<SnoozePreferences>
    suspend fun setSnooze(value: SnoozePreferences)

    fun observeGradualVolume(): PreferencesValue<GradualVolumePreferences>
    suspend fun setGradualVolume(value: GradualVolumePreferences)

    fun observeVibration(): PreferencesValue<VibrationPreferences>
    suspend fun setVibration(value: VibrationPreferences)

    fun observeAlarmTimeout(): PreferencesValue<AlarmTimeoutPreferences>
    suspend fun setAlarmTimeout(value: AlarmTimeoutPreferences)

    fun observeVolumeButtonsAction(): PreferencesValue<VolumeButtonsActionPreferences>
    suspend fun setVolumeButtonsAction(value: VolumeButtonsActionPreferences)

    fun observeTwentyFourHourTime(): PreferencesValue<TwentyFourHourTimePreferences>
    suspend fun setTwentyFourHourTime(value: TwentyFourHourTimePreferences)

    fun observeThemePreference(): PreferencesValue<ThemePreferencePreferences>
    suspend fun setThemePreference(value: ThemePreferencePreferences)

    suspend fun clear()
}

suspend fun AppSettingsRepository.awaitRingtone(timeout: Duration = DefaultTimeoutDuration): RingtonePreferences =
    observeRingtone().awaitFirstRealValue(timeout)

suspend fun AppSettingsRepository.getVibration(timeout: Duration = DefaultTimeoutDuration): VibrationPreferences =
    observeVibration().awaitFirstRealValue(timeout)
suspend fun AppSettingsRepository.getGradualVolume(timeout: Duration = DefaultTimeoutDuration): GradualVolumePreferences =
    observeGradualVolume().awaitFirstRealValue(timeout)
suspend fun AppSettingsRepository.getSnooze(timeout: Duration = DefaultTimeoutDuration): SnoozePreferences =
    observeSnooze().awaitFirstRealValue(timeout)
