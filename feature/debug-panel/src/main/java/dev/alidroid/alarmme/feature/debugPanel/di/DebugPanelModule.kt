package dev.alidroid.alarmme.feature.debugPanel.di

import dev.alidroid.alarmme.appSettings.AppSettingsRepository
import dev.alidroid.alarmme.feature.debugPanel.data.AlarmsWriteUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.AppBuildInfoUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.FeatureFlagsReadUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.FeatureFlagsUpdateUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.ResetAppSettingsUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.ResetAppToFirstLaunchUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.ResolveExactAlarmPermissionUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.data.ScheduledAlarmsCountUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.domain.AlarmsWriteUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ResetAppSettingsUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ResetAppToFirstLaunchUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ResolveExactAlarmPermissionUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ScheduleAlarmUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ScheduleAlarmUseCaseImpl
import dev.alidroid.alarmme.feature.debugPanel.domain.ScheduledAlarmsCountUseCase
import dev.alidroid.alarmme.feature.debugPanel.presentation.DebugPanelScreenModel
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository
import org.koin.dsl.module

val debugPanelModule = module {
    factory<FeatureFlagsReadUseCase> { FeatureFlagsReadUseCaseImpl(get()) }
    factory<FeatureFlagsUpdateUseCase> { FeatureFlagsUpdateUseCaseImpl(get()) }
    factory<ScheduledAlarmsCountUseCase> { ScheduledAlarmsCountUseCaseImpl(get()) }
    factory<ResolveExactAlarmPermissionUseCase> { ResolveExactAlarmPermissionUseCaseImpl() }
    factory<AppBuildInfoUseCase> { AppBuildInfoUseCaseImpl(get()) }
    factory<AlarmsWriteUseCase> { AlarmsWriteUseCaseImpl(get(), get()) }

    factory<ResetAppSettingsUseCase> { ResetAppSettingsUseCaseImpl(get<AppSettingsRepository>()) }
    factory<ResetAppToFirstLaunchUseCase> { ResetAppToFirstLaunchUseCaseImpl(get(), get(), get()) }
    factory<ScheduleAlarmUseCase> { ScheduleAlarmUseCaseImpl(get(), get(), get()) }

    factory { DebugPanelScreenModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}
