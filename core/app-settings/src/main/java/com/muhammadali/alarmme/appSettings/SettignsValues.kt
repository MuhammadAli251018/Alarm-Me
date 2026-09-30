package com.muhammadali.alarmme.appSettings

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface RingtonePreferences {
    @Serializable
    data object Default : RingtonePreferences

    @Serializable
    data class Ringtone(val uri: String) : RingtonePreferences
}

@Serializable
sealed interface SnoozePreferences {
    @Serializable
    data object NoSnooze : SnoozePreferences

    @Serializable
    data class SnoozeDuration(val duration: Duration, val repeats: Int) : SnoozePreferences
}

@Serializable
enum class GradualVolumePreferences {
    Enabled,
    Disabled
}

@Serializable
enum class VibrationPreferences {
    Enabled,
    Disabled
}

@Serializable
sealed interface AlarmTimeoutPreferences {
    @Serializable
    data object NoTimeout : AlarmTimeoutPreferences

    @Serializable
    data class TimeoutDuration(val duration: Duration) : AlarmTimeoutPreferences
}

@Serializable
enum class VolumeButtonsActionPreferences {
    Nothing,
    Mute,
    VolumeUpMutes,
    VolumeDownMutes
}

@Serializable
enum class TwentyFourHourTimePreferences {
    Enabled,
    Disabled
}

@Serializable
enum class ThemePreferencePreferences {
    System,
    Light,
    Dark
}