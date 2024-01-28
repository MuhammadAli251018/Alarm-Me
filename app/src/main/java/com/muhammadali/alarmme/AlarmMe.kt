package com.muhammadali.alarmme

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.muhammadali.alarmme.common.Notifications
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotificator
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmNotificatorImp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlarmMe : Application() {
    override fun onCreate() {
        super.onCreate()

        val alarmsChannel = NotificationChannel(
            AlarmNotificatorImp.ALARMS_CHANNEL_ID,
            AlarmNotificatorImp.ALARMS_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(alarmsChannel)

    }
}