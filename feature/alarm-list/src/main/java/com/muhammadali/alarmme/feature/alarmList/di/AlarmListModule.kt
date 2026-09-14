package com.muhammadali.alarmme.feature.alarmList.di

import com.muhammadali.alarmme.feature.alarmList.data.InMemoryAlarmsProvideUseCase
import com.muhammadali.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import com.muhammadali.alarmme.feature.alarmList.presentation.AlarmsListScreenModel
import org.koin.dsl.module

val alarmListModule = module {
    // A single, not a factory: the mock *is* the storage, so it has to outlive the screen that
    // edits it. Pointing this at the real use case is the whole of that migration.
    single<AlarmsProvideUseCase> { InMemoryAlarmsProvideUseCase() }

    factory { AlarmsListScreenModel(get()) }
}
