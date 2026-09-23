package com.muhammadali.alarmme

import android.app.Application
import com.muhammadali.alarmme.di.appDataModule
import com.muhammadali.alarmme.feature.alarmDetails.di.alarmDetailsModule
import com.muhammadali.alarmme.feature.alarmList.di.alarmListModule
import com.muhammadali.alarmme.ringtonePicker.di.ringtonePickerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AlarmMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlarmMeApplication)
            modules(
                appDataModule,
                alarmListModule,
                ringtonePickerModule,
                alarmDetailsModule,
            )
        }
    }
}
