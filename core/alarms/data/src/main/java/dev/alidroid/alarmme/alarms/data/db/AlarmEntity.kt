package dev.alidroid.alarmme.alarms.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String,
    val enabled: Boolean,
    val label: String,
    val timeIso: String,
    val scheduleType: String,
    val repeatDays: String,
    val customDates: String,
    val ringtoneId: String,
    val snoozeDurationSeconds: Long?,
    val snoozeCount: Int?,
    val vibrationEnabled: Boolean,
    val gradualVolumeEnabled: Boolean,
)

object AlarmScheduleType {
    const val REPEAT = "REPEAT"
    const val CUSTOM_DATES = "CUSTOM_DATES"
}
