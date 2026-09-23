package com.muhammadali.alarmme.feature.alarmDetails.di

import com.muhammadali.alarmme.feature.alarmDetails.data.AlarmDetailsUseCaseImpl
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import com.muhammadali.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreen
import com.muhammadali.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreenModel
import org.koin.dsl.module

val alarmDetailsModule = module {
    factory<AlarmDetailsUseCase> { AlarmDetailsUseCaseImpl(get(), get()) }

    factory { (startMode: AlarmDetailsScreen.StartMode) ->
        AlarmDetailsScreenModel(
            startMode,
            alarmDetailsUseCase = get<AlarmDetailsUseCase>()
        )
    }
}
