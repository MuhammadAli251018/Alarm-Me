package dev.alidroid.alarmme.feature.debugPanel.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.domain.SnoozePreferences
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import dev.alidroid.alarmme.utility.kotlinDateTime.plus
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val DEBUG_ALARM_LABEL = "Debug alarm"
private val DEBUG_ALARM_SNOOZE = SnoozePreferences(snoozeDuration = 5.minutes, snoozeCount = 3)

class ScheduleAlarmUseCaseImpl(
    private val alarmScheduler: AlarmScheduler,
    private val alarmsRepository: AlarmsRepository,
    private val ringtoneRepository: RingtoneRepository,
) : ScheduleAlarmUseCase {

    override suspend fun fireDebugAlarmIn(duration: Duration) {
        val fireAt = LocalDateTime.now() + duration
        val alarm = randomDebugAlarm(fireAt, ringtoneRepository.getDefaultRingtone())

        alarmsRepository.insertOrUpdate(alarm)
        alarmScheduler.scheduleOrUpdateAlarm(alarm.id, fireAt)
    }
}

suspend fun ScheduleAlarmUseCase.fireInstantAlarm() {
    fireDebugAlarmIn(100.milliseconds)
}

// TODO: better refering to a custom way of gnerating the alarm
@OptIn(ExperimentalUuidApi::class)
private fun randomDebugAlarm(fireAt: LocalDateTime, ringtone: Ringtone) = Alarm(
    id = AlarmId(Uuid.random().toString()),
    enabled = true,
    label = DEBUG_ALARM_LABEL,
    // The alarm reads as the moment it goes off, not the moment it was scheduled.
    time = fireAt.time,
    schedule = AlarmSchedule.CustomDates(listOf(fireAt.date)),
    preferences = AlarmPreferences(
        ringtone = ringtone,
        snoozePreferences = DEBUG_ALARM_SNOOZE,
        vibrationEnabled = true,
        gradualVolumeEnabled = false,
    )
)
