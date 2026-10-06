package dev.alidroid.alarmme.alarms.domain

import android.os.Parcelable
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@JvmInline
value class AlarmId(val value: String) : Parcelable {

    companion object {
        fun random(): AlarmId = AlarmId(UUID.randomUUID().toString())
    }
}

data class Alarm(
    val id: AlarmId,
    val enabled: Boolean,
    val label: String,
    val time: LocalTime,
    val schedule: AlarmSchedule,
    val preferences: AlarmPreferences
)