package dev.alidroid.alarmme.feature.alarmList.di

import dev.alidroid.alarmme.feature.alarmList.data.AlarmsProvideUseCaseImpl
import dev.alidroid.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import dev.alidroid.alarmme.feature.alarmList.presentation.AlarmsListScreenModel
import org.koin.dsl.module

val alarmListModule = module {
    factory<AlarmsProvideUseCase> { AlarmsProvideUseCaseImpl(get(), get()) }

    factory { AlarmsListScreenModel(get()) }
}
