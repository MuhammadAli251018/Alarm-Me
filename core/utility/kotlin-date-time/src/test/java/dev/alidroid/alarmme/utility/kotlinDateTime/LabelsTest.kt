package dev.alidroid.alarmme.utility.kotlinDateTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class LabelsTest : StringSpec({

    "no days reads as empty, leaving the wording to the caller" {
        emptySet<DayOfWeek>().toRangeLabel() shouldBe ""
    }

    "all seven days collapse to Every day" {
        DayOfWeek.entries.toSet().toRangeLabel() shouldBe "Every day"
    }

    "a run of three or more becomes a range" {
        setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ).toRangeLabel() shouldBe "Mon–Fri"
    }

    "scattered days are listed individually" {
        setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY).toRangeLabel() shouldBe
            "Mon, Wed, Fri"
    }

    "a pair stays listed rather than becoming a two-day range" {
        setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).toRangeLabel() shouldBe "Sat, Sun"
    }

    "a single day is just itself" {
        setOf(DayOfWeek.WEDNESDAY).toRangeLabel() shouldBe "Wed"
    }

    "a range and a stray day combine" {
        setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.SATURDAY
        ).toRangeLabel() shouldBe "Mon–Wed, Sat"
    }

    "input order does not matter" {
        setOf(DayOfWeek.FRIDAY, DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).toRangeLabel() shouldBe
            "Mon, Wed, Fri"
    }

    "no dates reads as empty" {
        emptyList<LocalDate>().toGroupedLabel() shouldBe ""
    }

    "dates in one month name it once, in ascending order" {
        listOf(
            LocalDate(2026, 9, 28),
            LocalDate(2026, 9, 20),
            LocalDate(2026, 9, 24)
        ).toGroupedLabel() shouldBe "Sep 20, 24, 28"
    }

    "dates spanning months are grouped per month" {
        listOf(
            LocalDate(2026, 10, 2),
            LocalDate(2026, 9, 28)
        ).toGroupedLabel() shouldBe "Sep 28 · Oct 2"
    }

    "a single date is just month and day" {
        listOf(LocalDate(2026, 9, 25)).toGroupedLabel() shouldBe "Sep 25"
    }
})
