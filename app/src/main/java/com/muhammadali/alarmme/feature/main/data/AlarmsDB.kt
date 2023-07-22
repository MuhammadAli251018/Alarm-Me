package com.muhammadali.alarmme.feature.main.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmsDB : RoomDatabase() {
    abstract fun alarmsDao(): AlarmsDao
}