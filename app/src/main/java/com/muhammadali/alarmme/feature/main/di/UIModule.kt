package com.muhammadali.alarmme.feature.main.di

import android.content.Context
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmReceiver
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmSchedulerImp
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object UIModule {

    /*@Provides
    fun providesInitialMainUIState() = MainUIState(listOf())*/

    @Provides
    fun providesTimeFormatter(): TimeDateFormatter {
        return TimeDateFormatter()
    }

    @Provides
    fun providesAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AlarmSchedulerImp(
            receiver = AlarmReceiver::class.java,
            context
        )
    }
}