package com.muhammadali.alarmme.feature.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    val time: Long,
    val title: String,
    val scheduled: Boolean,
    val repeat: String,
    val vibration: Boolean,
    val ringtoneRef: String,
    val snooze: String,
    val index: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)