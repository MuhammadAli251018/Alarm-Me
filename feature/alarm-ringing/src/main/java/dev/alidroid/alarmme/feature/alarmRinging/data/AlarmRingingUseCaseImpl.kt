package dev.alidroid.alarmme.feature.alarmRinging.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import kotlinx.coroutines.flow.Flow

class AlarmRingingUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
) : AlarmRingingUseCase {

    override fun observeRingingAlarm(id: AlarmId): Flow<Alarm?> =
        alarmsRepository.observeAlarm(id)
}
