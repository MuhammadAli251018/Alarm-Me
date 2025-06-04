package com.muhammadali.alarmme.di

import androidx.room.Room
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDB
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDao
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepoImp
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single<AlarmsDB> {
        Room.databaseBuilder(
            androidContext(),
            AlarmsDB::class.java,
            name = "alarms_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<AlarmsDao> {
        get<AlarmsDB>().alarmsDao()
    }

    factory<AlarmsDBRepo> {
        AlarmsDbRepoImp(
            dbDao = get<AlarmsDao>()
        )
    }
}