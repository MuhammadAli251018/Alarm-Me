package com.muhammadali.alarmme.feature.main.domain.entities

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.minutes

@Serializable
data class AlarmPreferences (
    val snooze: Snooze,
    val vibration: Boolean,
    val ringtoneRef: String,
    val repeat:RepeatPattern
) {

    @Serializable
    sealed class RepeatPattern {

        @Serializable
         class Weekly(val activeDays: Set<DaysOfWeeks>)
          : RepeatPattern() {

             //val activeDays: List<DaysOfWeeks> = activeDays.toSet().toList()

             @Serializable
            enum class DaysOfWeeks(val index: Int){
                Saturday(0),
                Sunday(1),
                Monday(2),
                Tuesday(3),
                Wednesday(4),
                Thursday(5),
                Friday(6)
            }
         }

        @Serializable
        data class CertainDays(val days: List<Long>) : RepeatPattern()
    }

    @Serializable
    sealed class Snooze (
        val repeat: Int,
        val duration: Duration,
        val id: Int
    ) {

        @Serializable
        object NoSnooze : Snooze(0, ZERO, 0)

        @Serializable
        /** Repeat for three times with 5 minutes duration*/
        object ThreeR5M : Snooze(3, 5.minutes, 1)

        @Serializable
        /** Repeat for 5 times with 5 minutes duration*/
        object FiveR5M : Snooze(5, 5.minutes, 2)
    }
}

fun getFromIndex(index: Int): AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks {
    return when(index) {
        0 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Saturday
        1 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Sunday
        2 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Monday
        3 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Tuesday
        4 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Wednesday
        5 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Thursday
        6 -> AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Friday
        else -> {
            throw Exception(""/*Todo*/)
        }
    }
}

@Serializable
data class Alarm(
    val alarmId: Int,
    val title: String,
    val time: Long,
    val preferences: AlarmPreferences,
    //Todo: Better to make it a sealed class rather than Boolean
    val enabled: Boolean,
)
