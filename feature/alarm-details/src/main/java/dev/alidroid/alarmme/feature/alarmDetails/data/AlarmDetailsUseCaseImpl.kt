package dev.alidroid.alarmme.feature.alarmDetails.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val NEW_ALARM_LABEL = ""

class AlarmDetailsUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val ringtoneRepository: RingtoneRepository,
) : AlarmDetailsUseCase {

    override fun getAlarm(id: AlarmId): Flow<Alarm?> = alarmsRepository.observeAlarm(id)

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun newAlarmDraft(): Alarm = Alarm(
        id = AlarmId(Uuid.random().toString()),
        enabled = true,
        label = NEW_ALARM_LABEL,
        time = LocalDateTime.now().time,
        schedule = AlarmSchedule.Repeat(emptySet()),
        preferences = AlarmPreferences(
            ringtone = ringtoneRepository.getDefaultRingtone(),
            snoozePreferences = null,
            vibrationEnabled = true,
            gradualVolumeEnabled = false,
        )
    )

    override suspend fun saveAlarm(alarm: Alarm) = alarmsRepository.insertOrUpdate(alarm)

    override suspend fun deleteAlarm(id: AlarmId) = alarmsRepository.delete(setOf(id))
}
