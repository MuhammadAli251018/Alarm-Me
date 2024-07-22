package com.muhammadali.alarmme.di

import android.content.Context
import androidx.room.Room
import com.muhammadali.alarmme.feature.main.data.AlarmsDB
import com.muhammadali.alarmme.feature.main.data.AlarmsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAlarmsDB(@ApplicationContext context: Context): AlarmsDB {
        return Room.databaseBuilder(context,
            AlarmsDB::class.java,
            name = "alarms_db").build()
    }

    @Provides
    fun provideAlarmDao(alarmsDb: AlarmsDB): AlarmsDao = alarmsDb.alarmsDao()
}