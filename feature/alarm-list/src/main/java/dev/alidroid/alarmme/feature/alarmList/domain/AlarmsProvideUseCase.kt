package dev.alidroid.alarmme.feature.alarmList.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmsProvideUseCase {
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun enableOrDisableAlarm(id: AlarmId, enabled: Boolean)
    suspend fun enableOrDisableAlarms(ids: Set<AlarmId>, enabled: Boolean)
    suspend fun deleteAlarms(ids: Set<AlarmId>)
}