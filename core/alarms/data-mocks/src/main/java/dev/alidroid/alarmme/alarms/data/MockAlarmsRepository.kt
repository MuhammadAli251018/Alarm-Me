package dev.alidroid.alarmme.alarms.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.SnoozePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes

class MockAlarmsRepository(initialAlarms: List<Alarm> = sampleAlarms()) : AlarmsRepository {

    private val alarms = MutableStateFlow(initialAlarms.associateBy { it.id })

    override fun observeAllAlarms(): Flow<List<Alarm>> = flow {
        emitAll(alarms.map { it.values.toList() })
    }

    override fun observeAlarm(id: AlarmId): Flow<Alarm?> = flow {
        emitAll(alarms.map { it[id] })
    }

    override suspend fun insertOrUpdate(alarm: Alarm) {
        alarms.update { it + (alarm.id to alarm) }
    }

    override suspend fun setEnabled(ids: Set<AlarmId>, enabled: Boolean) {
        alarms.update { current ->
            current.mapValues { (id, alarm) ->
                if (id in ids) alarm.copy(enabled = enabled) else alarm
            }
        }
    }

    override suspend fun delete(ids: Set<AlarmId>) {
        alarms.update { current -> current - ids }
    }

    override suspend fun deleteAll() {
        alarms.update { emptyMap() }
    }
}

internal fun sampleAlarms(): List<Alarm> {
    val ringtones = sampleRingtones()
    val weekdays = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
    )
    val everyDay = DayOfWeek.entries.toSet()

    return listOf(
        Alarm(
            id = AlarmId("gym"),
            enabled = true,
            label = "Gym — leave by six",
            time = LocalTime(6, 26),
            schedule = AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)),
            preferences = AlarmPreferences(
                ringtone = ringtones[0],
                snoozePreferences = SnoozePreferences(5.minutes, 2),
                vibrationEnabled = true,
                gradualVolumeEnabled = false,
            )
        ),
        Alarm(
            id = AlarmId("weekday"),
            enabled = true,
            label = "Weekday mornings, gently",
            time = LocalTime(7, 0),
            schedule = AlarmSchedule.Repeat(weekdays),
            preferences = AlarmPreferences(
                ringtone = ringtones[1],
                snoozePreferences = SnoozePreferences(9.minutes, 3),
                vibrationEnabled = false,
                gradualVolumeEnabled = true,
            )
        ),
        Alarm(
            id = AlarmId("standup"),
            enabled = true,
            label = "Standup in five",
            time = LocalTime(9, 15),
            schedule = AlarmSchedule.Repeat(weekdays),
            preferences = AlarmPreferences(
                ringtone = ringtones[2],
                snoozePreferences = SnoozePreferences(3.minutes, 1),
                vibrationEnabled = true,
                gradualVolumeEnabled = false,
            )
        ),
        Alarm(
            id = AlarmId("meds"),
            enabled = true,
            label = "Meds",
            time = LocalTime(12, 0),
            schedule = AlarmSchedule.Repeat(everyDay),
            preferences = AlarmPreferences(
                ringtone = ringtones[3],
                snoozePreferences = SnoozePreferences(10.minutes, 3),
                vibrationEnabled = true,
                gradualVolumeEnabled = true,
            )
        ),
        Alarm(
            id = AlarmId("bread"),
            enabled = false,
            label = "Take the bread out",
            time = LocalTime(15, 5),
            schedule = AlarmSchedule.CustomDates(listOf(LocalDate(2026, 9, 25))),
            preferences = AlarmPreferences(
                ringtone = ringtones[4],
                snoozePreferences = null,
                vibrationEnabled = false,
                gradualVolumeEnabled = false,
            )
        ),
        Alarm(
            id = AlarmId("bins"),
            enabled = true,
            label = "Bins out",
            time = LocalTime(19, 10),
            schedule = AlarmSchedule.Repeat(setOf(DayOfWeek.WEDNESDAY)),
            preferences = AlarmPreferences(
                ringtone = ringtones[0],
                snoozePreferences = SnoozePreferences(5.minutes, 1),
                vibrationEnabled = true,
                gradualVolumeEnabled = false,
            )
        ),
        Alarm(
            id = AlarmId("winddown"),
            enabled = false,
            label = "Wind down, screens off",
            time = LocalTime(21, 45),
            schedule = AlarmSchedule.Repeat(everyDay),
            preferences = AlarmPreferences(
                ringtone = ringtones[2],
                snoozePreferences = null,
                vibrationEnabled = false,
                gradualVolumeEnabled = true,
            )
        ),
    )
}