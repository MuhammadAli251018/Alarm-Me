package dev.alidroid.alarmme.alarms.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmEntity::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}

fun provideAlarmDatabase(context: Context): AlarmDatabase =
    Room.databaseBuilder(context, AlarmDatabase::class.java, "alarms.db").build()
