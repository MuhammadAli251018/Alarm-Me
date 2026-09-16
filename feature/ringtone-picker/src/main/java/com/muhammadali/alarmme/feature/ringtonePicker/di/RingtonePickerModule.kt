package com.muhammadali.alarmme.feature.ringtonePicker.di

import com.muhammadali.alarmme.feature.ringtonePicker.data.InMemoryRingtoneProvideUseCase
import com.muhammadali.alarmme.feature.ringtonePicker.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.RingtonePickerScreenModel
import org.koin.dsl.module

val ringtonePickerModule = module {
    // A single, not a factory: the mock *is* the storage, so it has to outlive the screen that
    // edits it. Pointing this at the real use case is the whole of that migration.
    single<RingtoneProvideUseCase> { InMemoryRingtoneProvideUseCase() }

    factory { RingtonePickerScreenModel(get()) }
}
