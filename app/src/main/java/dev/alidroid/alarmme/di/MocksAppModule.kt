package dev.alidroid.alarmme.di

import dev.alidroid.alarmme.alarms.data.MockAlarmsRepository
import dev.alidroid.alarmme.alarms.data.MockRingtoneRepository
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import org.koin.dsl.module

val mocksModule = module {
    // TODO: Add all mocks repositories and data sources here
    /*single<AlarmsRepository> { MockAlarmsRepository() }
    single<RingtoneRepository> { MockRingtoneRepository() }*/
}