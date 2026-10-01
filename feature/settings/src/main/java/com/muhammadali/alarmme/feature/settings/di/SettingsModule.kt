package com.muhammadali.alarmme.feature.settings.di

import com.muhammadali.alarmme.appSettings.AppSettings
import com.muhammadali.alarmme.feature.settings.data.AppVersionInfoUseCaseImpl
import com.muhammadali.alarmme.feature.settings.data.ResolveRingtoneTitleUseCaseImpl
import com.muhammadali.alarmme.feature.settings.data.SettingsReadUseCaseImpl
import com.muhammadali.alarmme.feature.settings.data.SettingsUpdateUseCaseImpl
import com.muhammadali.alarmme.feature.settings.domain.AppVersionInfoUseCase
import com.muhammadali.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase
import com.muhammadali.alarmme.feature.settings.domain.SettingsReadUseCase
import com.muhammadali.alarmme.feature.settings.domain.SettingsUpdateUseCase
import com.muhammadali.alarmme.feature.settings.presentation.SettingsScreenModel
import com.muhammadali.alarmme.preferences.PreferencesManager
import com.muhammadali.alarmme.preferences.PreferencesRepository
import org.koin.dsl.module

val settingsModule = module {
    factory<PreferencesRepository> { get<PreferencesManager>().getPreferenceOf(AppSettings.AppSettingsPrefRepoKey) }

    factory<SettingsReadUseCase> { SettingsReadUseCaseImpl() }
    factory<SettingsUpdateUseCase> { SettingsUpdateUseCaseImpl(get()) }
    factory<ResolveRingtoneTitleUseCase> { ResolveRingtoneTitleUseCaseImpl() }
    factory<AppVersionInfoUseCase> { AppVersionInfoUseCaseImpl() }

    factory { SettingsScreenModel(get(), get(), get(), get()) }
}
