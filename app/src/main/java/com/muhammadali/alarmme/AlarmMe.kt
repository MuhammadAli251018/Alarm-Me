package com.muhammadali.alarmme

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.muhammadali.alarmme.common.Notifications
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlarmMe : Application() {
    override fun onCreate() {
        super.onCreate()

        val alarmsChannel = NotificationChannel(
            Notifications.ALARMS_CHANNEL_ID,
            Notifications.ALARMS_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(alarmsChannel)

    }
}