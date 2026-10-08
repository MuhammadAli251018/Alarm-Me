package dev.alidroid.alarmme.feature.settings.presentation.model

import dev.alidroid.alarmme.appSettings.AlarmTimeoutPreferences
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.TwentyFourHourTimePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.VolumeButtonsActionPreferences
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfo
import dev.alidroid.alarmme.feature.settings.domain.SettingsSnapshot
import kotlin.time.Duration

/**
 * The domain → state boundary for the settings screen.
 */
internal fun SettingsSnapshot.toLoadedState(
    ringtoneTitle: String,
    appVersionInfo: AppVersionInfo,
    lengthOptions: List<Duration>,
    limitOptions: List<Int?>,
): SettingsState.Loaded = SettingsState.Loaded(
    soundLabel = ringtoneTitle,
    snoozeLengthIndex = snooze.toLengthIndex(lengthOptions),
    snoozeLimitIndex = snooze.toLimitIndex(limitOptions),
    snoozeLabel = snooze.toLabel(),
    lengthOptions = lengthOptions,
    limitOptions = limitOptions,
    gradualVolumeEnabled = gradualVolume == GradualVolumePreferences.Enabled,
    vibrateEnabled = vibration == VibrationPreferences.Enabled,
    silenceAfterLabel = alarmTimeout.toLabel(),
    volumeButtonsLabel = volumeButtonsAction.toLabel(),
    is24HourTimeEnabled = twentyFourHourTime == TwentyFourHourTimePreferences.Enabled,
    themeLabel = themePreference.name,
    versionLabel = appVersionInfo.versionLabel,
    showDeveloperSection = appVersionInfo.showDeveloperSection,
)

/** Resolves ScreenModel-side indices back into the value [dev.alidroid.alarmme.feature.settings.domain.SettingsUpdateUseCase.setSnooze] persists. */
internal fun snoozePreferencesOf(
    lengthIndex: Int?,
    limitIndex: Int?,
    lengthOptions: List<Duration>,
    limitOptions: List<Int?>,
): SnoozePreferences {
    val lengthIdx = lengthIndex ?: return SnoozePreferences.NoSnooze
    val limitValue = limitIndex?.let { limitOptions[it] } ?: Int.MAX_VALUE
    return SnoozePreferences.SnoozeDuration(lengthOptions[lengthIdx], limitValue)
}

private fun SnoozePreferences.toLengthIndex(lengthOptions: List<Duration>): Int? = when (this) {
    SnoozePreferences.NoSnooze -> null
    is SnoozePreferences.SnoozeDuration -> lengthOptions.indexOf(duration).takeIf { it >= 0 }
}

private fun SnoozePreferences.toLimitIndex(limitOptions: List<Int?>): Int? = when (this) {
    SnoozePreferences.NoSnooze -> null
    is SnoozePreferences.SnoozeDuration -> limitOptions.indexOfLimit(repeats)
}

/** `Int.MAX_VALUE` is this app's "no limit" sentinel, matched to the `null` (∞) box. */
private fun List<Int?>.indexOfLimit(count: Int): Int? = when {
    count == Int.MAX_VALUE -> indexOf(null)
    else -> indexOf(count).takeIf { it >= 0 }
}

private fun SnoozePreferences.toLabel(): String = when (this) {
    SnoozePreferences.NoSnooze -> "No snooze"
    is SnoozePreferences.SnoozeDuration -> if (repeats == Int.MAX_VALUE) {
        "${duration.inWholeMinutes} min · no limit"
    } else {
        "${duration.inWholeMinutes} min · $repeats times"
    }
}

private fun AlarmTimeoutPreferences.toLabel(): String = when (this) {
    AlarmTimeoutPreferences.NoTimeout -> "No timeout"
    is AlarmTimeoutPreferences.TimeoutDuration -> "${duration.inWholeMinutes} min"
}

/** "Mute" means the alarm snoozes, not that it goes silent — labelled accordingly. */
private fun VolumeButtonsActionPreferences.toLabel(): String = when (this) {
    VolumeButtonsActionPreferences.Nothing -> "Nothing"
    VolumeButtonsActionPreferences.Mute -> "Snooze"
    VolumeButtonsActionPreferences.VolumeUpMutes -> "Volume up · Snooze"
    VolumeButtonsActionPreferences.VolumeDownMutes -> "Volume down · Snooze"
}
