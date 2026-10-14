package dev.alidroid.alarmme.alarms.data

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.domain.SnoozePreferences
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.minutes

private val DEFAULT_RINGTONE = Ringtone(RingtoneId("default"), "Default", "uri-default", null)
private val CUSTOM_RINGTONE = Ringtone(RingtoneId("custom"), "Custom", "uri-custom", null)

private class FakeRingtoneRepository(
    private val ringtones: List<Ringtone> = listOf(DEFAULT_RINGTONE, CUSTOM_RINGTONE),
) : RingtoneRepository {
    override fun observeRingtones(): Flow<List<Ringtone>> = flowOf(ringtones)
    override fun observeRingtone(id: RingtoneId): Flow<Ringtone?> =
        flowOf(ringtones.firstOrNull { it.id == id })

    override fun getDefaultRingtone(): Ringtone = DEFAULT_RINGTONE
}

private fun alarmAt(
    schedule: AlarmSchedule,
    snoozePreferences: SnoozePreferences? = null,
    ringtone: Ringtone = CUSTOM_RINGTONE,
) = Alarm(
    id = AlarmId("test-id"),
    enabled = true,
    label = "Test",
    time = LocalTime(7, 30),
    schedule = schedule,
    preferences = AlarmPreferences(
        ringtone = ringtone,
        snoozePreferences = snoozePreferences,
        vibrationEnabled = true,
        gradualVolumeEnabled = false,
    ),
)

class AlarmEntityMapperTest : StringSpec({

    "a repeat schedule round-trips through the entity" {
        val alarm = alarmAt(AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)))

        val restored = alarm.toEntity().toDomain(FakeRingtoneRepository())

        restored shouldBe alarm
    }

    "a custom-dates schedule round-trips through the entity" {
        val alarm = alarmAt(
            AlarmSchedule.CustomDates(listOf(LocalDate(2026, 10, 5), LocalDate(2026, 11, 1)))
        )

        val restored = alarm.toEntity().toDomain(FakeRingtoneRepository())

        restored shouldBe alarm
    }

    "a null snooze preference round-trips as null" {
        val alarm = alarmAt(AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY)), snoozePreferences = null)

        val restored = alarm.toEntity().toDomain(FakeRingtoneRepository())

        restored.preferences.snoozePreferences shouldBe null
    }

    "a non-null snooze preference round-trips with its duration and count" {
        val alarm = alarmAt(
            AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY)),
            snoozePreferences = SnoozePreferences(snoozeDuration = 5.minutes, snoozeCount = 3),
        )

        val restored = alarm.toEntity().toDomain(FakeRingtoneRepository())

        restored.preferences.snoozePreferences shouldBe SnoozePreferences(5.minutes, 3)
    }

    "a resolvable ringtone id is restored to its full ringtone" {
        val alarm = alarmAt(AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY)), ringtone = CUSTOM_RINGTONE)

        val restored = alarm.toEntity().toDomain(FakeRingtoneRepository())

        restored.preferences.ringtone shouldBe CUSTOM_RINGTONE
    }

    "an unresolvable ringtone id falls back to the default ringtone" {
        val alarm = alarmAt(AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY)), ringtone = CUSTOM_RINGTONE)
        val entity = alarm.toEntity()
        val repositoryWithoutCustomRingtone = FakeRingtoneRepository(ringtones = listOf(DEFAULT_RINGTONE))

        val restored = entity.toDomain(repositoryWithoutCustomRingtone)

        restored.preferences.ringtone shouldBe DEFAULT_RINGTONE
    }
})
