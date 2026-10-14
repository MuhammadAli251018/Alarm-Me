package dev.alidroid.alarmme.di

import dev.alidroid.alarmme.AlarmMeApplication
import dev.alidroid.alarmme.AlarmReceiver
import dev.alidroid.alarmme.BuildConfig
import dev.alidroid.alarmme.alarms.data.MockAlarmsRepository
import dev.alidroid.alarmme.alarms.data.MockRingtoneRepository
import dev.alidroid.alarmme.alarms.data.RingtoneRepositoryImpl
import dev.alidroid.alarmme.alarms.data.provideAlarmsRepository
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.scheduler.AlarmScheduler
import dev.alidroid.alarmme.alarms.scheduler.AlarmSchedulerImpl
import dev.alidroid.alarmme.appSettings.AppInfo
import dev.alidroid.alarmme.appSettings.AppInfoRepository
import dev.alidroid.alarmme.appSettings.AppSettingsRepository
import dev.alidroid.alarmme.appSettings.AppSettingsRepositoryImpl
import dev.alidroid.alarmme.featureFlags.DebugPanelFeatureFlag
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepositoryImpl
import dev.alidroid.alarmme.preferences.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Qualifiers for the two interchangeable implementations of a repository. */
private val Real = named("real")
private val Mock = named("mock")

val appDataModule = module {
    // Both implementations stay registered. The unqualified binding below is a factory that reads
    // the feature flag on every resolution, so toggling a mock flag takes effect on the next screen
    // instead of requiring a process restart.
    single<AlarmsRepository>(Real) { provideAlarmsRepository(androidContext(), get(Real)) }
    single<RingtoneRepository>(Real) { RingtoneRepositoryImpl(androidContext()) }

    if (AlarmMeApplication.IS_DEV_FLAVOR) {
        single<AlarmsRepository>(Mock) { MockAlarmsRepository() }
        single<RingtoneRepository>(Mock) { MockRingtoneRepository() }
    }

    factory<AlarmsRepository> {
        val useMock = AlarmMeApplication.IS_DEV_FLAVOR &&
            get<FeatureFlagsRepository>().observeMockAlarms().value
        if (useMock) get<AlarmsRepository>(Mock) else get<AlarmsRepository>(Real)
    }

    factory<RingtoneRepository> {
        val useMock = AlarmMeApplication.IS_DEV_FLAVOR &&
            get<FeatureFlagsRepository>().observeMockRingtones().value
        if (useMock) get<RingtoneRepository>(Mock) else get<RingtoneRepository>(Real)
    }

    single<PreferencesManager> { PreferencesManager(androidContext(), CoroutineScope(SupervisorJob() + Dispatchers.Default)) }
    single<AlarmScheduler> { AlarmSchedulerImpl(androidContext(), AlarmReceiver::class.java) }
    single<DebugPanelFeatureFlag> { if (AlarmMeApplication.IS_DEV_FLAVOR) DebugPanelFeatureFlag.Enable else DebugPanelFeatureFlag.Disable }
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(get()) }
    single<FeatureFlagsRepository> { FeatureFlagsRepositoryImpl(get()) }
    single<AppInfoRepository> {
        AppInfoRepository {
            AppInfo(
                appVersion = BuildConfig.VERSION_NAME,
                appVersionCode = BuildConfig.VERSION_CODE,
                gitHashName = BuildConfig.GIT_HASH_NAME
            )
        }
    }
}
