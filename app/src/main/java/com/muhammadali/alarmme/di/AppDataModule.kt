package com.muhammadali.alarmme.di

import com.muhammadali.alarmme.alarms.data.MockRingtoneRepository
import com.muhammadali.alarmme.alarms.data.MockAlarmsRepository
import com.muhammadali.alarmme.alarms.domain.AlarmsRepository
import com.muhammadali.alarmme.alarms.domain.RingtoneRepository
import org.koin.dsl.module


val appDataModule = module {
    single<AlarmsRepository> { MockAlarmsRepository() }
    single<RingtoneRepository> { MockRingtoneRepository() }
}
