package dev.alidroid.alarmme.feature.settings.di

import dev.alidroid.alarmme.appSettings.AppSettings
import dev.alidroid.alarmme.feature.settings.data.AppVersionInfoUseCaseImpl
import dev.alidroid.alarmme.feature.settings.data.ResolveRingtoneTitleUseCaseImpl
import dev.alidroid.alarmme.feature.settings.data.SettingsReadUseCaseImpl
import dev.alidroid.alarmme.feature.settings.data.SettingsUpdateUseCaseImpl
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfoUseCase
import dev.alidroid.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsReadUseCase
import dev.alidroid.alarmme.feature.settings.domain.SettingsUpdateUseCase
import dev.alidroid.alarmme.feature.settings.presentation.SettingsScreenModel
import dev.alidroid.alarmme.preferences.PreferencesManager
import dev.alidroid.alarmme.preferences.PreferencesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule = module {
    factory<PreferencesRepository>(named("appSettings")) {
        get<PreferencesManager>().getPreferenceOf(AppSettings.AppSettingsPrefRepoKey)
    }

    factory<SettingsReadUseCase> { SettingsReadUseCaseImpl() }
    factory<SettingsUpdateUseCase> { SettingsUpdateUseCaseImpl(get(named("appSettings"))) }
    factory<ResolveRingtoneTitleUseCase> { ResolveRingtoneTitleUseCaseImpl() }
    factory<AppVersionInfoUseCase> { AppVersionInfoUseCaseImpl() }

    factory { SettingsScreenModel(get(), get(), get(), get()) }
}
