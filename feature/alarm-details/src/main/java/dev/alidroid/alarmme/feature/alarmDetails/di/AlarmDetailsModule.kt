package dev.alidroid.alarmme.feature.alarmDetails.di

import dev.alidroid.alarmme.feature.alarmDetails.data.AlarmDetailsUseCaseImpl
import dev.alidroid.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import dev.alidroid.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreen
import dev.alidroid.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreenModel
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
