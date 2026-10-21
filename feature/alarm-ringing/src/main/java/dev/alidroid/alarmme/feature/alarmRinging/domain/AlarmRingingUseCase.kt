package dev.alidroid.alarmme.feature.alarmRinging.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmRingingUseCase {
    fun observeRingingAlarm(id: AlarmId): Flow<Alarm?>

    suspend fun dismissRingingAlarm(id: AlarmId)
}
