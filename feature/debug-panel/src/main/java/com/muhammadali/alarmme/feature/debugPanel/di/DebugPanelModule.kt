package com.muhammadali.alarmme.feature.debugPanel.di

import com.muhammadali.alarmme.appSettings.AppSettings
import com.muhammadali.alarmme.feature.debugPanel.data.AlarmsWriteUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.AppBuildInfoUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.FeatureFlagsReadUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.FeatureFlagsUpdateUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.ResetAppSettingsUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.ResetAppToFirstLaunchUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.ResolveExactAlarmPermissionUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.ScheduledAlarmsCountUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.domain.AlarmsWriteUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ResetAppSettingsUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ResetAppToFirstLaunchUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ResolveExactAlarmPermissionUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ScheduledAlarmsCountUseCase
import com.muhammadali.alarmme.feature.debugPanel.presentation.DebugPanelScreenModel
import com.muhammadali.alarmme.featureFlags.FeatureFlags
import com.muhammadali.alarmme.preferences.PreferencesManager
import com.muhammadali.alarmme.preferences.PreferencesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val debugPanelModule = module {
    factory<PreferencesRepository>(named("featureFlags")) {
        get<PreferencesManager>().getPreferenceOf(FeatureFlags.FeatureFlagsPrefRepoKey)
    }

    factory<FeatureFlagsReadUseCase> { FeatureFlagsReadUseCaseImpl() }
    factory<FeatureFlagsUpdateUseCase> { FeatureFlagsUpdateUseCaseImpl(get(named("featureFlags"))) }
    factory<ScheduledAlarmsCountUseCase> { ScheduledAlarmsCountUseCaseImpl(get()) }
    factory<ResolveExactAlarmPermissionUseCase> { ResolveExactAlarmPermissionUseCaseImpl() }
    factory<AppBuildInfoUseCase> { AppBuildInfoUseCaseImpl() }
    factory<AlarmsWriteUseCase> { AlarmsWriteUseCaseImpl(get(), get()) }

    // Resolved through the manager instead of a named("appSettings") definition: settingsModule
    // already declares that qualifier, and a second one would silently override it.
    factory<ResetAppSettingsUseCase> {
        ResetAppSettingsUseCaseImpl(
            get<PreferencesManager>().getPreferenceOf(AppSettings.AppSettingsPrefRepoKey)
        )
    }
    factory<ResetAppToFirstLaunchUseCase> { ResetAppToFirstLaunchUseCaseImpl(get(), get(), get()) }

    factory { DebugPanelScreenModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}
