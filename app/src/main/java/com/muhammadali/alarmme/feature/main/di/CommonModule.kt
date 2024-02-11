package com.muhammadali.alarmme.feature.main.di

import android.content.BroadcastReceiver
import android.content.Context
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotificator
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmNotificatorImp
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmSchedulerImp
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class, ViewModelComponent::class, ActivityComponent::class)
object CommonModule {

    @Provides
    fun providesAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AlarmSchedulerImp(
            receiver = BroadcastReceiver::class.java,
            context = context
        )
    }

    @Provides
    fun providesNotificationCreator(@ApplicationContext context: Context): AlarmNotificator {
        return AlarmNotificatorImp(context = context)
    }

    @Provides
    fun providesTimeAdapter(): TimeAdapter = TimeAdapterImp()
}