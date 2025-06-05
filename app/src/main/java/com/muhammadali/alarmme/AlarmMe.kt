package com.muhammadali.alarmme

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.muhammadali.alarmme.di.dbModule
import com.muhammadali.alarmme.feature.main.di.commonModule
import com.muhammadali.alarmme.feature.main.di.uiModule
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmNotificatorImp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AlarmMe : Application() {
    override fun onCreate() {
        super.onCreate()

        // Setup Koin
        startKoin {
            androidLogger()
            androidContext(this@AlarmMe)
            modules(
                dbModule,
                commonModule,
                uiModule
            )
        }

        // Register notification channel Todo: Encapsulate in a function
        val alarmsChannel = NotificationChannel(
            AlarmNotificatorImp.ALARMS_CHANNEL_ID,
            AlarmNotificatorImp.ALARMS_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(alarmsChannel)

    }
}