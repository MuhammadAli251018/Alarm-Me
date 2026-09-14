package com.muhammadali.alarmme

import android.app.Application
import com.muhammadali.alarmme.feature.alarmList.di.alarmListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AlarmMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlarmMeApplication)
            modules(alarmListModule)
        }
    }
}
