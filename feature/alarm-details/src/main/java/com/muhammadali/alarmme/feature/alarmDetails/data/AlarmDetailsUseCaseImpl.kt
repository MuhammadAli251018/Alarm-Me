package com.muhammadali.alarmme.feature.alarmDetails.data

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.alarms.domain.AlarmPreferences
import com.muhammadali.alarmme.alarms.domain.AlarmSchedule
import com.muhammadali.alarmme.alarms.domain.AlarmsRepository
import com.muhammadali.alarmme.alarms.domain.RingtoneRepository
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import com.muhammadali.alarmme.utility.kotlinDateTime.now
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
