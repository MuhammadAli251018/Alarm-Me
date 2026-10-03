package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.AppSettings
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.ThemePreferencePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences
import dev.alidroid.alarmme.feature.settings.domain.SettingsReadUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/** `combine` only has direct overloads up to 5 distinct-typed flows, so the 8 settings are split into two groups and combined pairwise. */
private data class FirstFive(
    val ringtone: RingtonePreferences,
    val snooze: SnoozePreferences,
    val gradualVolume: GradualVolumePreferences,
    val vibration: VibrationPreferences,
    val alarmTimeout: AlarmTimeoutPreferences,
)

private data class LastThree(
    val volumeButtonsAction: VolumeButtonsActionPreferences,
    val twentyFourHourTime: TwentyFourHourTimePreferences,
    val themePreference: ThemePreferencePreferences,
)

class SettingsReadUseCaseImpl : SettingsReadUseCase {

    override fun observeSettings(): Flow<SettingsSnapshot> {
        val firstFive = combine(
            AppSettings.Ringtone.preferences,
            AppSettings.Snooze.preferences,
            AppSettings.GradualVolume.preferences,
            AppSettings.Vibration.preferences,
            AppSettings.AlarmTimeout.preferences,
        ) { ringtone, snooze, gradualVolume, vibration, alarmTimeout ->
            FirstFive(ringtone, snooze, gradualVolume, vibration, alarmTimeout)
        }

        val lastThree = combine(
            AppSettings.VolumeButtonsAction.preferences,
            AppSettings.TwentyFourHourTime.preferences,
            AppSettings.ThemePreference.preferences,
        ) { volumeButtonsAction, twentyFourHourTime, themePreference ->
            LastThree(volumeButtonsAction, twentyFourHourTime, themePreference)
        }

        return combine(firstFive, lastThree) { first, last ->
            SettingsSnapshot(
                ringtone = first.ringtone,
                snooze = first.snooze,
                gradualVolume = first.gradualVolume,
                vibration = first.vibration,
                alarmTimeout = first.alarmTimeout,
                volumeButtonsAction = last.volumeButtonsAction,
                twentyFourHourTime = last.twentyFourHourTime,
                themePreference = last.themePreference,
            )
        }
    }
}
