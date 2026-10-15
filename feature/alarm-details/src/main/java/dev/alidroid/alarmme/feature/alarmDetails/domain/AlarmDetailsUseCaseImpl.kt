package dev.alidroid.alarmme.feature.alarmDetails.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.alarms.scheduler.scheduleNextOccurrence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val NEW_ALARM_LABEL = "Untitled"

class AlarmDetailsUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val defaultAlarmRepository: DefaultAlarmRepository,
    private val alarmScheduler: AlarmScheduler,
) : AlarmDetailsUseCase {

    override fun getAlarm(id: AlarmId): Flow<Alarm?> = alarmsRepository.observeAlarm(id)

    override fun canSaveAlarm(alarm: Alarm): Flow<Boolean> =
        alarmsRepository.observeAlarm(alarm.id).map { savedAlarm ->
            // Case 1: Alarm don't exist
            if (savedAlarm == null) return@map true
            // Case 2: provided alarm is mutated over the saved one
            alarm.isAlarmMutated(savedAlarm)
        }

    private fun Alarm.isAlarmMutated(savedAlarm: Alarm): Boolean {
        return label != savedAlarm.label || time != savedAlarm.time ||
                schedule != savedAlarm.schedule || preferences != savedAlarm.preferences
    }

    override suspend fun newAlarmDraft(): Alarm {
        val (time, schedule) = defaultAlarmRepository.getAlarmTimeAndSchedule()

        return Alarm(
            id = AlarmId.random(),
            enabled = false,
            label = NEW_ALARM_LABEL,
            time = time,
            schedule = schedule,
            preferences = defaultAlarmRepository.getAlarmSettings()
        )
    }


    override suspend fun saveAlarm(alarm: Alarm) {
        alarmsRepository.insertOrUpdate(alarm)

        if (alarm.enabled) {
            alarmScheduler.scheduleNextOccurrence(alarm)
        } else {
            alarmScheduler.cancelAlarm(alarm.id)
        }
    }

    override suspend fun deleteAlarm(id: AlarmId) = alarmsRepository.delete(setOf(id))
}
