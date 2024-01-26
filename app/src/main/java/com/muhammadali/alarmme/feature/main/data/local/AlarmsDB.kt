package com.muhammadali.alarmme.feature.main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlarmEntity::class], version = 2)
abstract class AlarmsDB : RoomDatabase() {
    abstract fun alarmsDao(): AlarmsDao
}