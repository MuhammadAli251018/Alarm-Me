package com.muhammadali.alarmme.feature.alarmDetails.di

import com.muhammadali.alarmme.feature.alarmDetails.data.InMemoryAlarmDetailsUseCase
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsId
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import com.muhammadali.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreenModel
import org.koin.dsl.module

val alarmDetailsModule = module {
    single<AlarmDetailsUseCase> { InMemoryAlarmDetailsUseCase() }

    factory { (alarmId: AlarmDetailsId) -> AlarmDetailsScreenModel(alarmId, get()) }
}
