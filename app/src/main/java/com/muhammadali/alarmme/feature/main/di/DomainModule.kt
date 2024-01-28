package com.muhammadali.alarmme.feature.main.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
object DomainModule {

    /*@Provides
    fun providesAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun providesAlarmScheduler(alarmManager: AlarmManager, timeAdapter: TimeAdapter): AlarmScheduler {
        return AlarmSchedulerIMP(
            receiver = BroadcastReceiver::class.java,
            timeAdapter = timeAdapter
        )
    }

    @Provides
    fun providesNotificationCreator(notificationManager: NotificationManager): AlarmNotificator {
        return AlarmNotificationCreatorImp(notificationManager)
    }

    @Provides
    fun providesTimeAdapter(): TimeAdapter = TimeAdapterImp()*/
}