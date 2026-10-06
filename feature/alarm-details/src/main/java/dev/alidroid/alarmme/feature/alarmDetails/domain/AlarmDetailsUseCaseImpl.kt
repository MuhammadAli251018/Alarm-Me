package dev.alidroid.alarmme.feature.alarmDetails.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

private const val NEW_ALARM_LABEL = "Untitled"

class AlarmDetailsUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val defaultAlarmRepository: DefaultAlarmRepository
) : AlarmDetailsUseCase {

    override fun getAlarm(id: AlarmId): Flow<Alarm?> = alarmsRepository.observeAlarm(id)

    override suspend fun newAlarmDraft(): Alarm = Alarm(
        id = AlarmId.random(),
        enabled = true,
        label = NEW_ALARM_LABEL,
        time = LocalDateTime.now().time,
        schedule = defaultAlarmRepository.getAlarmSchedule(),
        preferences = defaultAlarmRepository.getAlarmSettings()
    )

    override suspend fun saveAlarm(alarm: Alarm) = alarmsRepository.insertOrUpdate(alarm)

    override suspend fun deleteAlarm(id: AlarmId) = alarmsRepository.delete(setOf(id))
}
