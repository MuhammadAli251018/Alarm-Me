package com.muhammadali.alarmme.alarms.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/** 2026-09-23 is a Wednesday, which every "which weekday" expectation below leans on. */
private val WEDNESDAY = LocalDate(2026, 9, 23)

private fun alarmAt(time: LocalTime, schedule: AlarmSchedule, enabled: Boolean = true) = Alarm(
    id = AlarmId("test"),
    enabled = enabled,
    label = "Test",
    time = time,
    schedule = schedule,
    preferences = AlarmPreferences(
        ringtone = Ringtone(RingtoneId("r"), "R", "uri", null),
        snoozePreferences = null,
        vibrationEnabled = false,
        gradualVolumeEnabled = false
    )
)

class AlarmOccurrenceTest : StringSpec({

    "repeat fires later today when the time has not passed" {
        val alarm = alarmAt(LocalTime(9, 0), AlarmSchedule.Repeat(setOf(DayOfWeek.WEDNESDAY)))

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(6, 0))) shouldBe
            LocalDateTime(WEDNESDAY, LocalTime(9, 0))
    }

    "repeat rolls to next week when today's time has already passed" {
        val alarm = alarmAt(LocalTime(6, 0), AlarmSchedule.Repeat(setOf(DayOfWeek.WEDNESDAY)))

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe
            LocalDateTime(LocalDate(2026, 9, 30), LocalTime(6, 0))
    }

    "repeat crossing midnight picks tomorrow, not a negative wait" {
        val alarm = alarmAt(LocalTime(0, 30), AlarmSchedule.Repeat(DayOfWeek.entries.toSet()))

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(23, 45))) shouldBe
            LocalDateTime(LocalDate(2026, 9, 24), LocalTime(0, 30))
    }

    "repeat picks the nearest of several days" {
        val alarm = alarmAt(
            time = LocalTime(6, 0),
            schedule = AlarmSchedule.Repeat(setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY))
        )

        // Wednesday -> Friday is nearer than next Monday.
        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe
            LocalDateTime(LocalDate(2026, 9, 25), LocalTime(6, 0))
    }

    "an empty repeat never fires" {
        val alarm = alarmAt(LocalTime(6, 0), AlarmSchedule.Repeat(emptySet()))

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe null
    }

    "custom dates pick the earliest still ahead" {
        val alarm = alarmAt(
            time = LocalTime(8, 0),
            schedule = AlarmSchedule.CustomDates(
                listOf(LocalDate(2026, 10, 5), LocalDate(2026, 9, 28), LocalDate(2026, 9, 24))
            )
        )

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe
            LocalDateTime(LocalDate(2026, 9, 24), LocalTime(8, 0))
    }

    "custom dates all in the past never fire" {
        val alarm = alarmAt(
            time = LocalTime(8, 0),
            schedule = AlarmSchedule.CustomDates(listOf(LocalDate(2026, 9, 1)))
        )

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe null
    }

    "today's custom date still counts while its time is ahead" {
        val alarm = alarmAt(
            time = LocalTime(18, 0),
            schedule = AlarmSchedule.CustomDates(listOf(WEDNESDAY))
        )

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe
            LocalDateTime(WEDNESDAY, LocalTime(18, 0))
    }

    "an empty custom date list never fires" {
        val alarm = alarmAt(LocalTime(8, 0), AlarmSchedule.CustomDates(emptyList()))

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(9, 0))) shouldBe null
    }

    "being disabled does not change the schedule — callers filter on enabled themselves" {
        val alarm = alarmAt(
            time = LocalTime(9, 0),
            schedule = AlarmSchedule.Repeat(setOf(DayOfWeek.WEDNESDAY)),
            enabled = false
        )

        alarm.nextOccurrence(LocalDateTime(WEDNESDAY, LocalTime(6, 0))) shouldBe
            LocalDateTime(WEDNESDAY, LocalTime(9, 0))
    }
})
