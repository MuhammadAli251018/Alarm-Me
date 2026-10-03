package dev.alidroid.alarmme.alarms.domain

import android.os.Parcelable
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parcelize

@Parcelize
@JvmInline
value class AlarmId(val value: String) : Parcelable

data class Alarm(
    val id: AlarmId,
    val enabled: Boolean,
    val label: String,
    val time: LocalTime,
    val schedule: AlarmSchedule,
    val preferences: AlarmPreferences
)