package com.muhammadali.alarmme.feature.main.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database
abstract class AlarmsDB : RoomDatabase() {
    abstract fun alarmsDao(): AlarmsDao
}