package com.muhammadali.alarmme.feature.main.data

import androidx.room.RoomDatabase

abstract class AlarmsDB : RoomDatabase() {
    abstract fun alarmsDao(): AlarmsDao
}