package com.muhammadali.alarmme.feature.main.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Long,
    val title: String,
    val scheduled: Boolean,
    val repeat: String,
    val vibration: Boolean,
    val ringtoneRef: String,
    val snooze: Int
)