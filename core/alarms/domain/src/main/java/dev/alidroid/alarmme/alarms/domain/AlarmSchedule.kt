package dev.alidroid.alarmme.alarms.domain

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

sealed interface AlarmSchedule {
    data class Repeat(val repeatDays: Set<DayOfWeek>) : AlarmSchedule {
        constructor(vararg repeatDays: DayOfWeek) : this(repeatDays.toSet())
    }
    data class CustomDates(val customDates: List<LocalDate>) : AlarmSchedule
}

val AlarmSchedule.isScheduled: Boolean
    get() = when(this) {
        is AlarmSchedule.CustomDates -> customDates.isNotEmpty()
        is AlarmSchedule.Repeat -> repeatDays.isNotEmpty()
    }