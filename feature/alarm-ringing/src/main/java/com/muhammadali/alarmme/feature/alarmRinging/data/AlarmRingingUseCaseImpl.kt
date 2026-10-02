package com.muhammadali.alarmme.feature.alarmRinging.data

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.alarms.domain.AlarmsRepository
import com.muhammadali.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import kotlinx.coroutines.flow.Flow

class AlarmRingingUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
) : AlarmRingingUseCase {

    override fun observeRingingAlarm(id: AlarmId): Flow<Alarm?> =
        alarmsRepository.observeAlarm(id)
}
