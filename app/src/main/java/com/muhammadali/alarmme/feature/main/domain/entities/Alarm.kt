package com.muhammadali.alarmme.feature.main.domain.entities

import kotlinx.serialization.Serializable

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

@Serializable
data class AlarmPreferences (
    val snooze: Boolean,
    val vibration: Boolean,
    val ringtone: AlarmRingtone,
    val repeat:RepeatPattern
) {

    @Serializable
    sealed class RepeatPattern {

        companion object {
            fun repeatOff() = AlarmPreferences.RepeatPattern.Weekly(setOf())
        }

        abstract val activeDays: Set<DaysOfWeeks>
        @Serializable
         open class Weekly(override val activeDays: Set<DaysOfWeeks>)
          : RepeatPattern() {

              @Serializable
              data class WeeklyPattern(val daysOfWeeks: Set<DaysOfWeeks>) : Weekly(daysOfWeeks)

          }
    }


    @Serializable
    data class AlarmRingtone(
        val name: String,
        val reference: String
    )
}


fun getFromIndex(index: Int): DaysOfWeeks {
    return when(index) {
        0 -> DaysOfWeeks.Saturday
        1 -> DaysOfWeeks.Sunday
        2 -> DaysOfWeeks.Monday
        3 -> DaysOfWeeks.Tuesday
        4 -> DaysOfWeeks.Wednesday
        5 -> DaysOfWeeks.Thursday
        6 -> DaysOfWeeks.Friday
        else -> {
            throw Exception(""/*Todo*/)
        }
    }
}

@Serializable
data class Alarm(
    val alarmId: Int,
    val title: String,
    //Todo: Better to make it a sealed class rather than Boolean
    val enabled: Boolean,
    val time: Long,
    val preferences: AlarmPreferences,
)
