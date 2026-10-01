package com.muhammadali.alarmme.feature.debugPanel.di

import com.muhammadali.alarmme.feature.debugPanel.data.AppBuildInfoUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.FeatureFlagsReadUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.FeatureFlagsUpdateUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.ResolveExactAlarmPermissionUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.data.ScheduledAlarmsCountUseCaseImpl
import com.muhammadali.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
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

    factory { DebugPanelScreenModel(get(), get(), get(), get(), get()) }
}
