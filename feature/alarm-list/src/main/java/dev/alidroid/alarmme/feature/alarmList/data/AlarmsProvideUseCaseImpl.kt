package dev.alidroid.alarmme.feature.alarmList.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.alarms.scheduler.scheduleNextOccurrence
import dev.alidroid.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class AlarmsProvideUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val alarmScheduler: AlarmScheduler,
) : AlarmsProvideUseCase {

    override fun getAlarms(): Flow<List<Alarm>> = alarmsRepository.observeAllAlarms()

    override suspend fun enableOrDisableAlarm(id: AlarmId, enabled: Boolean) =
        enableOrDisableAlarms(setOf(id), enabled)

    override suspend fun enableOrDisableAlarms(ids: Set<AlarmId>, enabled: Boolean) {
        alarmsRepository.setEnabled(ids, enabled)

        // Read back after the write so each alarm carries the flag it was just given. One query
        // for the whole set rather than one per id.
        val alarms = alarmsRepository.observeAllAlarms().first().filter { it.id in ids }

        alarms.forEach { alarm ->
            if (enabled) {
                alarmScheduler.scheduleNextOccurrence(alarm)
            } else {
                alarmScheduler.cancelAlarm(alarm.id)
            }
        }
    }

    override suspend fun deleteAlarms(ids: Set<AlarmId>) {
        ids.forEach { id -> alarmScheduler.cancelAlarm(id) }

        alarmsRepository.delete(ids)
    }
}
