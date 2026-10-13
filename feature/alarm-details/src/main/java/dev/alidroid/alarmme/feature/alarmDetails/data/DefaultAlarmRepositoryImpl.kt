package dev.alidroid.alarmme.feature.alarmDetails.data

import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.domain.getRingtone
import dev.alidroid.alarmme.appSettings.AppSettingsRepository
import dev.alidroid.alarmme.appSettings.GradualVolumePreferences
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.appSettings.SnoozePreferences
import dev.alidroid.alarmme.appSettings.VibrationPreferences
import dev.alidroid.alarmme.appSettings.getGradualVolume
import dev.alidroid.alarmme.appSettings.getRingtone
import dev.alidroid.alarmme.appSettings.getSnooze
import dev.alidroid.alarmme.appSettings.getVibration
import dev.alidroid.alarmme.feature.alarmDetails.domain.DefaultAlarmRepository
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.LocalDateTime

private typealias DomainSnoozeSettings = dev.alidroid.alarmme.alarms.domain.SnoozePreferences

class DefaultAlarmRepositoryImpl(
    private val ringtoneRepository: RingtoneRepository,
    private val appSettingsRepository: AppSettingsRepository,
) : DefaultAlarmRepository {

    override suspend fun getAlarmSettings(): AlarmPreferences {
        val ringtone = appSettingsRepository.getRingtone().toDomain(ringtoneRepository)

        return AlarmPreferences(
            ringtone = ringtone,
            snoozePreferences = appSettingsRepository.getSnooze().toDomain(),
            vibrationEnabled = appSettingsRepository.getVibration().isEnabled(),
            gradualVolumeEnabled = appSettingsRepository.getGradualVolume().isEnabled()
        )
    }

    override suspend fun getAlarmSchedule(): AlarmSchedule {
        val today = LocalDateTime.now().dayOfWeek
        return AlarmSchedule.Repeat(today)
    }

}

private suspend fun RingtonePreferences.toDomain(ringtoneRepository: RingtoneRepository): Ringtone {
    return when(this) {
        RingtonePreferences.Default -> ringtoneRepository.getDefaultRingtone()
        is RingtonePreferences.Ringtone ->
            ringtoneRepository.getRingtone(RingtoneId(id)) ?: ringtoneRepository.getDefaultRingtone()
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

