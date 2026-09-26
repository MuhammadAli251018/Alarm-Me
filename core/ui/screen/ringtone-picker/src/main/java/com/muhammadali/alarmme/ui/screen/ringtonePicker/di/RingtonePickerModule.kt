package com.muhammadali.alarmme.ui.screen.ringtonePicker.di

import com.muhammadali.alarmme.ui.screen.ringtonePicker.data.RingtoneProvideUseCaseImpl
import com.muhammadali.alarmme.ui.screen.ringtonePicker.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.RingtonePickerScreenModel
import org.koin.dsl.module

val ringtonePickerModule = module {
    // A factory, not a single: storage is the shared RingtoneRepository (bound once in the app
    // module), so this use case holds nothing worth outliving the screen that uses it.
    factory<RingtoneProvideUseCase> { RingtoneProvideUseCaseImpl(get()) }

    factory { RingtonePickerScreenModel(get()) }
}
