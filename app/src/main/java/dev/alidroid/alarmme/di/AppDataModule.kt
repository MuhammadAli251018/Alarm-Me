package dev.alidroid.alarmme.di

import dev.alidroid.alarmme.AlarmReceiver
import dev.alidroid.alarmme.alarms.data.MockRingtoneRepository
import dev.alidroid.alarmme.alarms.data.MockAlarmsRepository
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.alarms.scheduler.AlarmSchedulerImpl
import dev.alidroid.alarmme.preferences.PreferencesManager
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
