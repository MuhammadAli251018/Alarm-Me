package dev.alidroid.alarmme.feature.alarmList.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import kotlinx.coroutines.flow.Flow


class AlarmsProvideUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
) : AlarmsProvideUseCase {

    override fun getAlarms(): Flow<List<Alarm>> = alarmsRepository.observeAllAlarms()

    override suspend fun enableOrDisableAlarm(id: AlarmId, enabled: Boolean) =
        alarmsRepository.setEnabled(setOf(id), enabled)

    override suspend fun enableOrDisableAlarms(ids: Set<AlarmId>, enabled: Boolean) =
        alarmsRepository.setEnabled(ids, enabled)

    override suspend fun deleteAlarms(ids: Set<AlarmId>) = alarmsRepository.delete(ids)
}
