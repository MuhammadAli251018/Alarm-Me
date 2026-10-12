package dev.alidroid.alarmme.feature.settings.di

import dev.alidroid.alarmme.feature.settings.data.AppVersionInfoUseCaseImpl
import dev.alidroid.alarmme.feature.settings.data.ResolveRingtoneTitleUseCaseImpl
import dev.alidroid.alarmme.feature.settings.data.SettingsReadUseCaseImpl
import dev.alidroid.alarmme.feature.settings.data.SettingsUpdateUseCaseImpl
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfoUseCase
import dev.alidroid.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsReadUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsUpdateUseCase
import dev.alidroid.alarmme.feature.settings.presentation.SettingsScreenModel
import org.koin.dsl.module

val settingsModule = module {
    factory<SettingsReadUseCase> { SettingsReadUseCaseImpl(get()) }
    factory<SettingsUpdateUseCase> { SettingsUpdateUseCaseImpl(get()) }
    factory<ResolveRingtoneTitleUseCase> { ResolveRingtoneTitleUseCaseImpl(get()) }
    factory<AppVersionInfoUseCase> { AppVersionInfoUseCaseImpl() }

    factory { SettingsScreenModel(get(), get(), get(), get()) }
}
