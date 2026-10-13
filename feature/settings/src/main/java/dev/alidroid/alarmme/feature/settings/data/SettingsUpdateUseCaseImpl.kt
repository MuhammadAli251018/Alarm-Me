package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.AppSettingsRepository
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.ThemePreferencePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences
import dev.alidroid.alarmme.feature.settings.domain.SettingsUpdateUseCase

class SettingsUpdateUseCaseImpl(
    private val appSettingsRepository: AppSettingsRepository,
) : SettingsUpdateUseCase {

    override suspend fun setRingtone(value: RingtonePreferences) =
        appSettingsRepository.setRingtone(value)

    override suspend fun setSnooze(value: SnoozePreferences) =
        appSettingsRepository.setSnooze(value)

    override suspend fun setGradualVolume(value: GradualVolumePreferences) =
        appSettingsRepository.setGradualVolume(value)

    override suspend fun setVibration(value: VibrationPreferences) =
        appSettingsRepository.setVibration(value)

    override suspend fun setAlarmTimeout(value: AlarmTimeoutPreferences) =
        appSettingsRepository.setAlarmTimeout(value)

    override suspend fun setVolumeButtonsAction(value: VolumeButtonsActionPreferences) =
        appSettingsRepository.setVolumeButtonsAction(value)

    override suspend fun setTwentyFourHourTime(value: TwentyFourHourTimePreferences) =
        appSettingsRepository.setTwentyFourHourTime(value)

    override suspend fun setThemePreference(value: ThemePreferencePreferences) =
        appSettingsRepository.setThemePreference(value)
}
