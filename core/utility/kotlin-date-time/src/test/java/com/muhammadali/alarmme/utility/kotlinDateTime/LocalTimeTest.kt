package com.muhammadali.alarmme.utility.kotlinDateTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalTime

class LocalTimeTest : StringSpec({

    "hour and minute are both zero-padded" {
        LocalTime(6, 40).toClockLabel() shouldBe "06:40"
    }

    "midnight reads as 12, not 00" {
        LocalTime(0, 40).toClockLabel() shouldBe "12:40"
    }

    "noon reads as 12, not 00" {
        LocalTime(12, 0).toClockLabel() shouldBe "12:00"
    }

    "afternoon hours fold back into the 1-12 range" {
        LocalTime(13, 5).toClockLabel() shouldBe "01:05"
    }
})
