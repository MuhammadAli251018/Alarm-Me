package dev.alidroid.alarmme.feature.alarmRinging.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.utility.flow.AsyncResult

interface AlarmRingingUseCase {
    suspend fun getRingingAlarm(id: AlarmId): AsyncResult<Alarm>

    suspend fun dismissRingingAlarm(id: AlarmId)
}
