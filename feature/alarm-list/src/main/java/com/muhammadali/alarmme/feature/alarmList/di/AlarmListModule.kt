package com.muhammadali.alarmme.feature.alarmList.di

import com.muhammadali.alarmme.feature.alarmList.data.AlarmsProvideUseCaseImpl
import com.muhammadali.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import com.muhammadali.alarmme.feature.alarmList.presentation.AlarmsListScreenModel
import org.koin.dsl.module

val alarmListModule = module {
    factory<AlarmsProvideUseCase> { AlarmsProvideUseCaseImpl(get()) }

    factory { AlarmsListScreenModel(get()) }
}
