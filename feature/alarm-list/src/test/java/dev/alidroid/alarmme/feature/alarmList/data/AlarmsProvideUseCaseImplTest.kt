package dev.alidroid.alarmme.feature.alarmList.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
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

// Fixed custom dates keep nextOccurrence deterministic: a CustomDates schedule resolves to exactly
// LocalDateTime(date, time) whatever the wall clock says, unlike a Repeat schedule.
private val FUTURE_DATE = LocalDate(2099, 1, 1)
private val PAST_DATE = LocalDate(2000, 1, 1)

private val RINGTONE = Ringtone(RingtoneId("default"), "Default", "uri-default", null)

private fun alarm(
    id: String,
    enabled: Boolean = false,
    dates: List<LocalDate> = listOf(FUTURE_DATE),
) = Alarm(
    id = AlarmId(id),
    enabled = enabled,
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

    fun stored(id: String): Alarm? = state.value.firstOrNull { it.id == AlarmId(id) }
}

private class RecordingAlarmScheduler : AlarmScheduler {
    val scheduled = mutableMapOf<AlarmId, LocalDateTime>()
    val cancelled = mutableListOf<AlarmId>()

    override suspend fun scheduleOrUpdateAlarm(alarmId: AlarmId, time: LocalDateTime) {
        scheduled[alarmId] = time
    }

    override suspend fun cancelAlarm(alarmId: AlarmId): Boolean {
        cancelled += alarmId
        return true
    }
}

class AlarmsProvideUseCaseImplTest : StringSpec({

    "enabling an alarm arms it at its next occurrence and stores the flag" {
        val repository = FakeAlarmsRepository(listOf(alarm("a", enabled = false)))
        val scheduler = RecordingAlarmScheduler()

        AlarmsProvideUseCaseImpl(repository, scheduler).enableOrDisableAlarm(AlarmId("a"), true)

        scheduler.scheduled[AlarmId("a")] shouldBe LocalDateTime(FUTURE_DATE, ALARM_TIME)
        scheduler.cancelled shouldBe emptyList()
        repository.stored("a")?.enabled shouldBe true
    }

    "disabling an alarm cancels it and stores the flag" {
        val repository = FakeAlarmsRepository(listOf(alarm("a", enabled = true)))
        val scheduler = RecordingAlarmScheduler()

        AlarmsProvideUseCaseImpl(repository, scheduler).enableOrDisableAlarm(AlarmId("a"), false)

        scheduler.cancelled shouldBe listOf(AlarmId("a"))
        scheduler.scheduled.isEmpty() shouldBe true
        repository.stored("a")?.enabled shouldBe false
    }

    // Agreed behaviour: an alarm whose dates have all passed still reads as enabled. It just has
    // nothing to arm until its schedule is edited.
    "enabling an alarm with no future occurrence stores the flag and arms nothing" {
        val repository = FakeAlarmsRepository(listOf(alarm("a", dates = listOf(PAST_DATE))))
        val scheduler = RecordingAlarmScheduler()

        AlarmsProvideUseCaseImpl(repository, scheduler).enableOrDisableAlarm(AlarmId("a"), true)

        scheduler.scheduled.isEmpty() shouldBe true
        repository.stored("a")?.enabled shouldBe true
    }

    "a bulk toggle arms every alarm in the set and leaves the rest alone" {
        val repository = FakeAlarmsRepository(
            listOf(alarm("a"), alarm("b"), alarm("untouched"))
        )
        val scheduler = RecordingAlarmScheduler()

        AlarmsProvideUseCaseImpl(repository, scheduler)
            .enableOrDisableAlarms(setOf(AlarmId("a"), AlarmId("b")), true)

        scheduler.scheduled.keys shouldBe setOf(AlarmId("a"), AlarmId("b"))
        repository.stored("a")?.enabled shouldBe true
        repository.stored("b")?.enabled shouldBe true
        repository.stored("untouched")?.enabled shouldBe false
    }

    // A pending system alarm outliving the row it belongs to would fire for an alarm that no
    // longer exists.
    "deleting alarms cancels them before removing the rows" {
        val repository = FakeAlarmsRepository(listOf(alarm("a", enabled = true), alarm("b")))
        val scheduler = RecordingAlarmScheduler()

        AlarmsProvideUseCaseImpl(repository, scheduler).deleteAlarms(setOf(AlarmId("a")))

        scheduler.cancelled shouldBe listOf(AlarmId("a"))
        repository.stored("a") shouldBe null
        repository.stored("b") shouldBe alarm("b")
    }
})
