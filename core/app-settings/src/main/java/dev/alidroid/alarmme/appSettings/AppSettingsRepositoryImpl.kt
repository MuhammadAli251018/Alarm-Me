package dev.alidroid.alarmme.appSettings

import dev.alidroid.alarmme.preferences.PrefRepositoryKey
import dev.alidroid.alarmme.preferences.PreferencesKey
import dev.alidroid.alarmme.preferences.PreferencesManager
import dev.alidroid.alarmme.preferences.PreferencesValue
import dev.alidroid.alarmme.preferences.getPrefValue
import dev.alidroid.alarmme.preferences.setPrefValue

private val AppSettingsPrefRepoKey = PrefRepositoryKey("app_settings")

class AppSettingsRepositoryImpl(preferencesManager: PreferencesManager) : AppSettingsRepository {

    private val preferences = preferencesManager.getPreferenceOf(AppSettingsPrefRepoKey)

    private val ringtone: PreferencesValue<RingtonePreferences> =
        preferences.getPrefValue(PreferencesKey("ringtone"), RingtonePreferences.Default)
    private val snooze:  PreferencesValue<SnoozePreferences> =
        preferences.getPrefValue(PreferencesKey("snooze"), SnoozePreferences.NoSnooze)
    private val gradualVolume: PreferencesValue<GradualVolumePreferences> =
        preferences.getPrefValue(PreferencesKey("gradual_volume"), GradualVolumePreferences.Disabled)
    private val vibration: PreferencesValue<VibrationPreferences> =
        preferences.getPrefValue(PreferencesKey("vibration"), VibrationPreferences.Disabled)
    private val alarmTimeout: PreferencesValue<AlarmTimeoutPreferences> =
        preferences.getPrefValue(PreferencesKey("alarm_timeout"), AlarmTimeoutPreferences.NoTimeout)
    private val volumeButtonsAction: PreferencesValue<VolumeButtonsActionPreferences> =
        preferences.getPrefValue(PreferencesKey("volume_buttons_action"), VolumeButtonsActionPreferences.Nothing)
    private val twentyFourHourTime: PreferencesValue<TwentyFourHourTimePreferences> =
        preferences.getPrefValue(PreferencesKey("twenty_four_hour_time"), TwentyFourHourTimePreferences.Disabled)
    private val themePreference: PreferencesValue<ThemePreferencePreferences> =
        preferences.getPrefValue(PreferencesKey("theme_preference"), ThemePreferencePreferences.System)

    override fun observeRingtone(): PreferencesValue<RingtonePreferences> = ringtone
    override suspend fun setRingtone(value: RingtonePreferences) = preferences.setPrefValue(ringtone.key, value)

    override fun observeSnooze(): PreferencesValue<SnoozePreferences> = snooze
    override suspend fun setSnooze(value: SnoozePreferences) = preferences.setPrefValue(snooze.key, value)

    override fun observeGradualVolume(): PreferencesValue<GradualVolumePreferences> = gradualVolume
    override suspend fun setGradualVolume(value: GradualVolumePreferences) =
        preferences.setPrefValue(gradualVolume.key, value)

    override fun observeVibration(): PreferencesValue<VibrationPreferences> = vibration
    override suspend fun setVibration(value: VibrationPreferences) = preferences.setPrefValue(vibration.key, value)

    override fun observeAlarmTimeout(): PreferencesValue<AlarmTimeoutPreferences> = alarmTimeout
    override suspend fun setAlarmTimeout(value: AlarmTimeoutPreferences) =
        preferences.setPrefValue(alarmTimeout.key, value)

    override fun observeVolumeButtonsAction(): PreferencesValue<VolumeButtonsActionPreferences> = volumeButtonsAction
    override suspend fun setVolumeButtonsAction(value: VolumeButtonsActionPreferences) =
        preferences.setPrefValue(volumeButtonsAction.key, value)

    override fun observeTwentyFourHourTime(): PreferencesValue<TwentyFourHourTimePreferences> = twentyFourHourTime
    override suspend fun setTwentyFourHourTime(value: TwentyFourHourTimePreferences) =
        preferences.setPrefValue(twentyFourHourTime.key, value)

    override fun observeThemePreference(): PreferencesValue<ThemePreferencePreferences> = themePreference
    override suspend fun setThemePreference(value: ThemePreferencePreferences) =
        preferences.setPrefValue(themePreference.key, value)

    override suspend fun clear() = preferences.clear()
}
