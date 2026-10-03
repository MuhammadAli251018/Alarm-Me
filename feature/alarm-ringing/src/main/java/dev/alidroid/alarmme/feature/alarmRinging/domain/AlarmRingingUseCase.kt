package dev.alidroid.alarmme.feature.alarmRinging.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmRingingUseCase {
    /** Emits null when nothing is stored under [id] — a stale or unknown alarm. */
    fun observeRingingAlarm(id: AlarmId): Flow<Alarm?>
}
