package dev.alidroid.alarmme.feature.alarmRinging.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.getAlarm
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.alarms.scheduler.scheduleNextOccurrence
import dev.alidroid.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class AlarmRingingUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val alarmScheduler: AlarmScheduler,
) : AlarmRingingUseCase {

    override fun observeRingingAlarm(id: AlarmId): Flow<Alarm?> =
        alarmsRepository.observeAlarm(id)

    override suspend fun dismissRingingAlarm(id: AlarmId) {
        val alarm = alarmsRepository.getAlarm(id) ?: return

        // Counted from the occurrence that just rang rather than straight off the clock: a delivery
        // landing a moment early would leave today's occurrence still in the future, and
        // nextOccurrence would hand back the very alarm being dismissed and re-arm it seconds away.
        val now = LocalDateTime.now()
        val firedAt = LocalDateTime(now.date, alarm.time)

        alarmScheduler.scheduleNextOccurrence(alarm, now = maxOf(now, firedAt))
    }
}
