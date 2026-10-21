package dev.alidroid.alarmme.feature.alarmRinging.di

import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.feature.alarmRinging.data.AlarmRingingUseCaseImpl
import dev.alidroid.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import dev.alidroid.alarmme.feature.alarmRinging.presentation.AlarmRingingScreenModel
import org.koin.dsl.module

val alarmRingingModule = module {
    factory<AlarmRingingUseCase> { AlarmRingingUseCaseImpl(get(), get(), get()) }

    factory { (alarmId: AlarmId) ->
        AlarmRingingScreenModel(
            alarmId = alarmId,
            alarmRingingUseCase = get<AlarmRingingUseCase>()
        )
    }
}
