package dev.alidroid.alarmme.feature.alarmDetails.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmDetailsUseCase {
    fun getAlarm(id: AlarmId): Flow<Alarm?>

    fun canSaveAlarm(alarm: Alarm): Flow<Boolean>

    suspend fun newAlarmDraft(): Alarm

    suspend fun saveAlarm(alarm: Alarm)

    suspend fun deleteAlarm(id: AlarmId)
}
