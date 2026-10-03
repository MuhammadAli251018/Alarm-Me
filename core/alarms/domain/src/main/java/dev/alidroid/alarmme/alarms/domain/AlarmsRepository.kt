package dev.alidroid.alarmme.alarms.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

interface AlarmsRepository {

    fun observeAllAlarms(): Flow<List<Alarm>>

    fun observeAlarm(id: AlarmId): Flow<Alarm?>

    suspend fun insertOrUpdate(alarm: Alarm)

    suspend fun setEnabled(ids: Set<AlarmId>, enabled: Boolean)

    suspend fun delete(ids: Set<AlarmId>)

    suspend fun deleteAll()
}

suspend fun AlarmsRepository.getAlarm(id: AlarmId): Alarm? = observeAlarm(id).firstOrNull()

suspend fun AlarmsRepository.setEnabled(id: AlarmId, enabled: Boolean) =
    setEnabled(setOf(id), enabled)

suspend fun AlarmsRepository.delete(id: AlarmId) = delete(setOf(id))