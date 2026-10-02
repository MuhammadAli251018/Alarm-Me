package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.alarms.domain.AlarmPreferences
import com.muhammadali.alarmme.alarms.domain.AlarmSchedule
import com.muhammadali.alarmme.alarms.domain.AlarmsRepository
import com.muhammadali.alarmme.alarms.domain.RingtoneRepository
import com.muhammadali.alarmme.alarms.domain.SnoozePreferences
import com.muhammadali.alarmme.feature.debugPanel.domain.AlarmsWriteUseCase
import com.muhammadali.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val WEEKDAYS = setOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
)

private val MOCK_SNOOZE = SnoozePreferences(snoozeDuration = 5.minutes, snoozeCount = 3)

class AlarmsWriteUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
    private val ringtoneRepository: RingtoneRepository,
) : AlarmsWriteUseCase {

    override suspend fun flushAlarms() = alarmsRepository.deleteAll()

    override suspend fun writeMockAlarms(alarmsCount: Int) {
        mockAlarms(alarmsCount).forEach { alarm -> alarmsRepository.insertOrUpdate(alarm) }
    }

    /**
     * Alarms spread over the day with a varied mix of schedules and preferences. Ids are random
     * so [AlarmsRepository.insertOrUpdate] can't collapse two of them into one entry.
     */
    @OptIn(ExperimentalUuidApi::class)
    private fun mockAlarms(count: Int): List<Alarm> {
        val ringtone = ringtoneRepository.getDefaultRingtone()
        val today = LocalDateTime.now().date

        return List(count) { index ->
            Alarm(
                id = AlarmId(Uuid.random().toString()),
                enabled = index % 5 != 0,
                label = "Mock alarm ${index + 1}",
                time = LocalTime(hour = (5 + index) % 24, minute = (index * 7) % 60),
                schedule = when (index % 4) {
                    0 -> AlarmSchedule.Repeat(WEEKDAYS)
                    1 -> AlarmSchedule.Repeat(DayOfWeek.entries.toSet())
                    2 -> AlarmSchedule.Repeat(setOf(DayOfWeek.entries[index % DayOfWeek.entries.size]))
                    else -> AlarmSchedule.CustomDates(listOf(today))
                },
                preferences = AlarmPreferences(
                    ringtone = ringtone,
                    snoozePreferences = if (index % 3 == 0) null else MOCK_SNOOZE,
                    vibrationEnabled = index % 2 == 0,
                    gradualVolumeEnabled = index % 3 == 0,
                )
            )
        }
    }
}
