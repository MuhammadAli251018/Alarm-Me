package com.muhammadali.alarmme.feature.alarmRinging.domain

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmRingingUseCase {
    /** Emits null when nothing is stored under [id] — a stale or unknown alarm. */
    fun observeRingingAlarm(id: AlarmId): Flow<Alarm?>
}
