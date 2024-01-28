package com.muhammadali.alarmme.feature.main.domain.repositories

import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmsDBRepo {

    suspend fun addOrUpdateAlarm(alarm: Alarm): Result<Unit>

    suspend fun deleteAlarm(alarmId: Int): Result<Unit>

   fun getAllAlarms(): Flow<Result<List<Alarm>>>

   fun getAlarmWithId(id: Int): Flow<Result<Alarm>>
}