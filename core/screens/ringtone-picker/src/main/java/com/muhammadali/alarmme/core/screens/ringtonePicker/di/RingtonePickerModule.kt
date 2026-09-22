package com.muhammadali.alarmme.core.screens.ringtonePicker.di

import com.muhammadali.alarmme.core.screens.ringtonePicker.data.InMemoryRingtoneProvideUseCase
import com.muhammadali.alarmme.core.screens.ringtonePicker.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.core.screens.ringtonePicker.presentatoin.RingtonePickerScreenModel
import org.koin.dsl.module

val ringtonePickerModule = module {
    // A single, not a factory: the mock *is* the storage, so it has to outlive the screen that
    // edits it. Pointing this at the real use case is the whole of that migration.
    single<RingtoneProvideUseCase> { InMemoryRingtoneProvideUseCase() }

    factory { RingtonePickerScreenModel(get()) }
}
