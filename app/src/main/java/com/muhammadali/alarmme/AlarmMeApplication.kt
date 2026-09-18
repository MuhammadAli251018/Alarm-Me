package com.muhammadali.alarmme

import android.app.Application
import com.muhammadali.alarmme.feature.alarmDetails.di.alarmDetailsModule
import com.muhammadali.alarmme.feature.alarmList.di.alarmListModule
import com.muhammadali.alarmme.feature.ringtonePicker.di.ringtonePickerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AlarmMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlarmMeApplication)
            modules(alarmListModule, ringtonePickerModule, alarmDetailsModule)
        }
    }
}
