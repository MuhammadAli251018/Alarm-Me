package com.muhammadali.alarmme.di

import com.muhammadali.alarmme.AlarmReceiver
import com.muhammadali.alarmme.alarms.data.MockRingtoneRepository
import com.muhammadali.alarmme.alarms.data.MockAlarmsRepository
import com.muhammadali.alarmme.alarms.domain.AlarmsRepository
import com.muhammadali.alarmme.alarms.domain.RingtoneRepository
import com.muhammadali.alarmme.alarms.scheduler.AlarmScheduler
import com.muhammadali.alarmme.alarms.scheduler.AlarmSchedulerImpl
import com.muhammadali.alarmme.preferences.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appDataModule = module {
    single<AlarmsRepository> { MockAlarmsRepository() }
    single<RingtoneRepository> { MockRingtoneRepository() }
    single { PreferencesManager(androidContext(), CoroutineScope(SupervisorJob() + Dispatchers.Default)) }

    single<AlarmScheduler> { AlarmSchedulerImpl(androidContext(), AlarmReceiver::class.java) }
}
