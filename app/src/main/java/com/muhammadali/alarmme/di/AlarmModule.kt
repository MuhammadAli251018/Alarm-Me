package com.muhammadali.alarmme.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.muhammadali.alarmme.feature.main.domain.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.AlarmSchedulerImp
import com.muhammadali.alarmme.feature.main.domain.AlarmCreator
import com.muhammadali.alarmme.feature.main.domain.AlarmCreatorImp
import dagger.Module
import dagger.Provides
//import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
//@InstallIn(AlarmService::class)
object AlarmModule {

    @Provides
    fun providesAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun providesAlarmScheduler(alarmManager: AlarmManager): AlarmScheduler {
        return AlarmSchedulerImp(alarmManager)
    }

    @Provides
    fun providesNotificationCreator(notificationManager: NotificationManager): AlarmCreator {
        return AlarmCreatorImp(notificationManager)
    }
}