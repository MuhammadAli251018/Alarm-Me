package com.muhammadali.alarmme.feature.main.domain.entities

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.minutes

data class AlarmPreferences (
    val snooze: Snooze,
    val vibration: Boolean,
    val ringtoneRef: String,
    val repeat:RepeatPattern
) {
    sealed class RepeatPattern {

         class DaysInWeek (
             vararg  activeDays: DaysOfWeeks
         ) : RepeatPattern() {

            val activeDays: List<DaysOfWeeks> = activeDays.toSet().toList()

            enum class DaysOfWeeks{
                Saturday,
                Sunday,
                Monday,
                Tuesday,
                Wednesday,
                Thursday,
                Friday
            }
         }

        data class CertainDays(val days: List<String>) : RepeatPattern()
    }

    sealed class Snooze (
        val repeat: Int,
        val duration: Duration
    ) {

        object NoSnooze : Snooze(0, ZERO)

        /** Repeat for three times with 5 minutes duration*/
        object ThreeR5M : Snooze(3, 5.minutes)

        /** Repeat for 5 times with 5 minutes duration*/
        object FiveR5M : Snooze(5, 5.minutes)
    }
}

data class Alarm(
    val alarmId: String,
    val title: String,
    val time: String,
    val preferences: AlarmPreferences,
)
