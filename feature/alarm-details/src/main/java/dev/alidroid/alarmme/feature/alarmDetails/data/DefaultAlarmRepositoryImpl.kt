package dev.alidroid.alarmme.feature.alarmDetails.data

import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.appSettings.AppSettings
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.feature.alarmDetails.domain.DefaultAlarmRepository
import dev.alidroid.alarmme.preferences.PreferencesValue
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.LocalDateTime

private typealias DomainSnoozeSettings = dev.alidroid.alarmme.alarms.domain.SnoozePreferences

class DefaultAlarmRepositoryImpl(
    private val ringtoneRepository: RingtoneRepository,
    private val defaultVibration: PreferencesValue<VibrationPreferences> = AppSettings.Vibration.preferences,
    val defaultGradualVolume: PreferencesValue<GradualVolumePreferences> = AppSettings.GradualVolume.preferences,
    val defaultSnooze: PreferencesValue<SnoozePreferences> = AppSettings.Snooze.preferences
) : DefaultAlarmRepository {

    override suspend fun getAlarmSettings(): AlarmPreferences {
        return AlarmPreferences(
            ringtone = ringtoneRepository.getDefaultRingtone(),
            snoozePreferences = defaultSnooze.value.toDomain(),
            vibrationEnabled = defaultVibration.value.isEnabled(),
            gradualVolumeEnabled = defaultGradualVolume.value.isEnabled()
        )
    }

    override suspend fun getAlarmSchedule(): AlarmSchedule {
        val today = LocalDateTime.now().dayOfWeek
        return AlarmSchedule.Repeat(today)
    }


}

private fun SnoozePreferences.toDomain(): DomainSnoozeSettings? = when (this) {
    SnoozePreferences.NoSnooze -> null
    is SnoozePreferences.SnoozeDuration -> DomainSnoozeSettings(
        snoozeDuration = duration,
        snoozeCount = repeats
    )
}

private fun VibrationPreferences.isEnabled(): Boolean =
    this == VibrationPreferences.Enabled

private fun GradualVolumePreferences.isEnabled(): Boolean =
    this == GradualVolumePreferences.Enabled

