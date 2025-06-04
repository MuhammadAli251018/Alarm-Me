package com.muhammadali.alarmme.feature.main.di

import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotificator
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmNotificatorImp
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmReceiver
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmSchedulerImp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonModule = module {
    factory<AlarmScheduler> {
        AlarmSchedulerImp(
            receiver = AlarmReceiver::class.java,
            context = androidContext()
        )
    }

    factory<AlarmNotificator> {
        AlarmNotificatorImp(context = androidContext())
    }

    factory<TimeAdapter> {
        TimeAdapterImp()
    }
}