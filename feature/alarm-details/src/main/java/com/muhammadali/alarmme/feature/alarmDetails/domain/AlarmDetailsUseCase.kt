package com.muhammadali.alarmme.feature.alarmDetails.domain

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmDetailsUseCase {
    fun getAlarm(id: AlarmId): Flow<Alarm?>

    suspend fun newAlarmDraft(): Alarm

    suspend fun saveAlarm(alarm: Alarm)

    suspend fun deleteAlarm(id: AlarmId)
}
