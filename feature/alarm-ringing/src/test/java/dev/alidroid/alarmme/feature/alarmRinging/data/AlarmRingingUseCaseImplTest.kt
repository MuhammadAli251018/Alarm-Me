package dev.alidroid.alarmme.feature.alarmRinging.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

private val ALARM_TIME = LocalTime(7, 0)

// Fixed dates keep nextOccurrence deterministic: a CustomDates schedule resolves to exactly
// LocalDateTime(date, time) whatever the wall clock says. The repeat arithmetic itself is already
// covered by AlarmOccurrenceTest.
private val LATER_DATE = LocalDate(2099, 1, 1)
private val PAST_DATE = LocalDate(2000, 1, 1)

private val RINGING_ID = AlarmId("ringing")

private val RINGTONE = Ringtone(RingtoneId("default"), "Default", "uri-default", null)

private fun alarm(dates: List<LocalDate>) = Alarm(
    id = RINGING_ID,
    enabled = true,
    label = "Test",
    time = ALARM_TIME,
    schedule = AlarmSchedule.CustomDates(dates),
    preferences = AlarmPreferences(
        ringtone = RINGTONE,
        snoozePreferences = null,
        vibrationEnabled = false,
        gradualVolumeEnabled = false,
    )
)

private class FakeAlarmsRepository(alarms: List<Alarm>) : AlarmsRepository {
    private val state = MutableStateFlow(alarms)

    override fun observeAllAlarms(): Flow<List<Alarm>> = state

    override fun observeAlarm(id: AlarmId): Flow<Alarm?> =
        state.map { alarms -> alarms.firstOrNull { it.id == id } }

    override suspend fun insertOrUpdate(alarm: Alarm) {
        state.update { alarms -> alarms.filterNot { it.id == alarm.id } + alarm }
    }

    override suspend fun setEnabled(ids: Set<AlarmId>, enabled: Boolean) {
        state.update { alarms ->
            alarms.map { if (it.id in ids) it.copy(enabled = enabled) else it }
        }
    }

    override suspend fun delete(ids: Set<AlarmId>) {
        state.update { alarms -> alarms.filterNot { it.id in ids } }
    }

    override suspend fun deleteAll() {
        state.value = emptyList()
    }
}

private class RecordingAlarmScheduler : AlarmScheduler {
    val scheduled = mutableMapOf<AlarmId, LocalDateTime>()

    override suspend fun scheduleOrUpdateAlarm(alarmId: AlarmId, time: LocalDateTime) {
        scheduled[alarmId] = time
    }

    override suspend fun cancelAlarm(alarmId: AlarmId): Boolean = true
}

class AlarmRingingUseCaseImplTest : StringSpec({

    "dismissing re-arms the alarm at its next remaining occurrence" {
        val repository = FakeAlarmsRepository(listOf(alarm(dates = listOf(PAST_DATE, LATER_DATE))))
        val scheduler = RecordingAlarmScheduler()

        AlarmRingingUseCaseImpl(repository, scheduler).dismissRingingAlarm(RINGING_ID)

        scheduler.scheduled[RINGING_ID] shouldBe LocalDateTime(LATER_DATE, ALARM_TIME)
    }

    // The early-delivery guard: counting from the occurrence that just rang rather than from the
    // clock stops today's own occurrence being re-armed seconds away.
    "dismissing a one-shot due today arms nothing" {
        val today = LocalDateTime.now().date
        val repository = FakeAlarmsRepository(listOf(alarm(dates = listOf(today))))
        val scheduler = RecordingAlarmScheduler()

        AlarmRingingUseCaseImpl(repository, scheduler).dismissRingingAlarm(RINGING_ID)

        scheduler.scheduled.isEmpty() shouldBe true
    }

    "dismissing an alarm whose dates have all passed arms nothing" {
        val repository = FakeAlarmsRepository(listOf(alarm(dates = listOf(PAST_DATE))))
        val scheduler = RecordingAlarmScheduler()

        AlarmRingingUseCaseImpl(repository, scheduler).dismissRingingAlarm(RINGING_ID)

        scheduler.scheduled.isEmpty() shouldBe true
    }

    "dismissing an id with no alarm behind it arms nothing and does not throw" {
        val repository = FakeAlarmsRepository(emptyList())
        val scheduler = RecordingAlarmScheduler()

        AlarmRingingUseCaseImpl(repository, scheduler).dismissRingingAlarm(RINGING_ID)

        scheduler.scheduled.isEmpty() shouldBe true
    }
})
