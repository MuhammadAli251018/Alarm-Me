package com.muhammadali.alarmme.feature.alarmDetails.domain

import kotlinx.coroutines.flow.Flow

interface AlarmDetailsUseCase {
    fun getAlarmDetails(id: AlarmDetailsId): Flow<AlarmDetails?>
    suspend fun saveAlarmDetails(details: AlarmDetails)
    suspend fun deleteAlarmDetails(id: AlarmDetailsId)
}
