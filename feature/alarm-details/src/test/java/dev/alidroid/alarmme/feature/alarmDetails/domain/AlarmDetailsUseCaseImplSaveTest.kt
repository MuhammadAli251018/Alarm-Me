package dev.alidroid.alarmme.feature.alarmDetails.domain

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
private val LATER_TIME = LocalTime(9, 30)

// A CustomDates schedule resolves to exactly LocalDateTime(date, time) whatever the wall clock
// says, which keeps these assertions independent of when they run.
private val FUTURE_DATE = LocalDate(2099, 1, 1)

private val RINGTONE = Ringtone(RingtoneId("default"), "Default", "uri-default", null)

private fun alarm(enabled: Boolean, time: LocalTime = ALARM_TIME) = Alarm(
    id = AlarmId("a"),
    enabled = enabled,
    label = "Test",
    time = time,
    schedule = AlarmSchedule.CustomDates(listOf(FUTURE_DATE)),
    preferences = AlarmPreferences(
        ringtone = RINGTONE,
        snoozePreferences = null,
        vibrationEnabled = false,
        gradualVolumeEnabled = false,
    )
)

private class FakeAlarmsRepository : AlarmsRepository {
    private val state = MutableStateFlow(emptyList<Alarm>())

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

/** [DefaultAlarmRepository] plays no part in saving, so these cases never reach it. */
private class UnusedDefaultAlarmRepository : DefaultAlarmRepository {
    override suspend fun getAlarmSettings(): AlarmPreferences = error("not used when saving")
    override fun getAlarmSchedule(): AlarmSchedule = error("not used when saving")
    override fun getAlarmTime(): LocalTime = error("not used when saving")
}

private fun useCase(
    repository: AlarmsRepository,
    scheduler: AlarmScheduler,
) = AlarmDetailsUseCaseImpl(repository, UnusedDefaultAlarmRepository(), scheduler)

class AlarmDetailsUseCaseImplSaveTest : StringSpec({

    "saving an enabled alarm persists it and arms it at its next occurrence" {
        val repository = FakeAlarmsRepository()
        val scheduler = RecordingAlarmScheduler()

        useCase(repository, scheduler).saveAlarm(alarm(enabled = true))

        repository.stored("a")?.enabled shouldBe true
        scheduler.scheduled[AlarmId("a")] shouldBe LocalDateTime(FUTURE_DATE, ALARM_TIME)
        scheduler.cancelled shouldBe emptyList()
    }

    "saving a disabled alarm persists it and cancels any armed alarm" {
        val repository = FakeAlarmsRepository()
        val scheduler = RecordingAlarmScheduler()

        useCase(repository, scheduler).saveAlarm(alarm(enabled = false))

        repository.stored("a")?.enabled shouldBe false
        scheduler.cancelled shouldBe listOf(AlarmId("a"))
        scheduler.scheduled.isEmpty() shouldBe true
    }

    // The reason saving re-arms on every save rather than only for a new alarm: otherwise an
    // edited time leaves the old system alarm standing.
    "re-saving an enabled alarm with a changed time re-arms it at the new time" {
        val repository = FakeAlarmsRepository()
        val scheduler = RecordingAlarmScheduler()
        val subject = useCase(repository, scheduler)

        subject.saveAlarm(alarm(enabled = true))
        subject.saveAlarm(alarm(enabled = true, time = LATER_TIME))

        repository.stored("a")?.time shouldBe LATER_TIME
        scheduler.scheduled[AlarmId("a")] shouldBe LocalDateTime(FUTURE_DATE, LATER_TIME)
    }
})
