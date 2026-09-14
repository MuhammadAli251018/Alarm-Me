package com.muhammadali.alarmme.feature.alarmList.domain

import kotlinx.coroutines.flow.Flow

interface AlarmsProvideUseCase {
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun enableOrDisableAlarm(id: AlarmItemId, enabled: Boolean)
    suspend fun enableOrDisableAlarm(items: Map<AlarmItemId, Boolean>)
    suspend fun deleteAlarms(ids: List<AlarmItemId>)
}