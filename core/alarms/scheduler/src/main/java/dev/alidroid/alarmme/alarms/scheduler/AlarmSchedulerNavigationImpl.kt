package dev.alidroid.alarmme.alarms.scheduler

import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.utility.kotlinDateTime.minus
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

/** Uses Navigation to fire an alarm, used for testing.*/
class AlarmSchedulerNavigationImpl(
    private val onAlarm: (AlarmId) -> Unit,
    private val scope: CoroutineScope
) : AlarmScheduler {

    private val alarms = mutableMapOf<AlarmId, Job>()

    override suspend fun scheduleOrUpdateAlarm(alarmId: AlarmId, time: LocalDateTime) {
        // Canceling older job in case it's still running.
        cancelAlarm(alarmId)

        val alarmJob = scope.launch {
            val timeToAlarm = time - LocalDateTime.now()
            delay(timeToAlarm)
            onAlarm(alarmId)
        }

        alarms += alarmId to alarmJob
    }

    override suspend fun cancelAlarm(alarmId: AlarmId): Boolean {
        return alarms.remove(alarmId)?.cancel()?.let { true } ?: false
    }
}