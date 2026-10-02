package com.muhammadali.alarmme.feature.alarmRinging.di

import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.feature.alarmRinging.data.AlarmRingingUseCaseImpl
import com.muhammadali.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import com.muhammadali.alarmme.feature.alarmRinging.presentation.AlarmRingingScreenModel
import org.koin.dsl.module

val alarmRingingModule = module {
    factory<AlarmRingingUseCase> { AlarmRingingUseCaseImpl(get()) }

    factory { (alarmId: AlarmId) ->
        AlarmRingingScreenModel(
            alarmId = alarmId,
            alarmRingingUseCase = get<AlarmRingingUseCase>()
        )
    }
}
