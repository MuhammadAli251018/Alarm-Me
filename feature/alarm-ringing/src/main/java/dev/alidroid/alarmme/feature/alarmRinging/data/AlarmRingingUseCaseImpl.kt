package dev.alidroid.alarmme.feature.alarmRinging.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.getAlarm
import dev.alidroid.alarmme.alarms.scheduler.AlarmRingingController
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.alarms.scheduler.scheduleNextOccurrence
import dev.alidroid.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import dev.alidroid.alarmme.utility.flow.AsyncResult
import dev.alidroid.alarmme.utility.flow.asAsyncResult


class AlarmRingingUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val alarmScheduler: AlarmScheduler,
    private val alarmRingingController: AlarmRingingController,
) : AlarmRingingUseCase {


    override suspend fun getRingingAlarm(id: AlarmId): AsyncResult<Alarm> =
        alarmsRepository.observeAlarm(id).asAsyncResult()

    override suspend fun dismissRingingAlarm(id: AlarmId) {
        // Ahead of the guard below: an alarm deleted while it rang still has to be silenced.
        alarmRingingController.stopRinging(id)

        val alarm = alarmsRepository.getAlarm(id) ?: return
        alarmScheduler.scheduleNextOccurrence(alarm)
    }
}
