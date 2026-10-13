package dev.alidroid.alarmme.appSettings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface AppSettingsRepository {
    fun observeRingtone(): Flow<RingtonePreferences>
    suspend fun setRingtone(value: RingtonePreferences)

    fun observeSnooze(): Flow<SnoozePreferences>
    suspend fun setSnooze(value: SnoozePreferences)

    fun observeGradualVolume(): Flow<GradualVolumePreferences>
    suspend fun setGradualVolume(value: GradualVolumePreferences)

    fun observeVibration(): Flow<VibrationPreferences>
    suspend fun setVibration(value: VibrationPreferences)

    fun observeAlarmTimeout(): Flow<AlarmTimeoutPreferences>
    suspend fun setAlarmTimeout(value: AlarmTimeoutPreferences)

    fun observeVolumeButtonsAction(): Flow<VolumeButtonsActionPreferences>
    suspend fun setVolumeButtonsAction(value: VolumeButtonsActionPreferences)

    fun observeTwentyFourHourTime(): Flow<TwentyFourHourTimePreferences>
    suspend fun setTwentyFourHourTime(value: TwentyFourHourTimePreferences)

    fun observeThemePreference(): Flow<ThemePreferencePreferences>
    suspend fun setThemePreference(value: ThemePreferencePreferences)

    suspend fun clear()
}

suspend fun AppSettingsRepository.getRingtone(): RingtonePreferences = observeRingtone().first()

suspend fun AppSettingsRepository.getVibration(): VibrationPreferences = observeVibration().first()
suspend fun AppSettingsRepository.getGradualVolume(): GradualVolumePreferences = observeGradualVolume().first()
suspend fun AppSettingsRepository.getSnooze(): SnoozePreferences = observeSnooze().first()
