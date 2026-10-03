package dev.alidroid.alarmme.utility.kotlinDateTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class LocalDateTimeTest : StringSpec({

    "positive gap when this is later than other" {
        val later = LocalDateTime(2026, 9, 29, 10, 30)
        val earlier = LocalDateTime(2026, 9, 29, 9, 0)

        later - earlier shouldBe 1.hours + 30.minutes
    }

    "negative gap when this is earlier than other" {
        val earlier = LocalDateTime(2026, 9, 29, 9, 0)
        val later = LocalDateTime(2026, 9, 29, 10, 30)

        earlier - later shouldBe -(1.hours + 30.minutes)
    }

    "zero when both are the same instant" {
        val moment = LocalDateTime(2026, 9, 29, 9, 0)

        moment - moment shouldBe 0.minutes
    }
})
