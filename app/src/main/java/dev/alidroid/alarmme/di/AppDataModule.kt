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

private val Real = named("real")
private val Mock = named("mock")

val appModule = module {
    single<AlarmScheduler> { AlarmSchedulerImpl(androidContext(), AlarmReceiver::class.java) }
}

val GlobalCoroutineScopeId = named("global-scope")
val appPreferencesModule = module {
    single<CoroutineScope>(GlobalCoroutineScopeId) { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single<PreferencesManager> { PreferencesManager(androidContext(), get(named("global-scope"))) }
    single<DebugPanelFeatureFlag> { if (AlarmMeApplication.IS_DEV_FLAVOR) DebugPanelFeatureFlag.Enable else DebugPanelFeatureFlag.Disable }
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(get()) }
    single<FeatureFlagsRepository> { FeatureFlagsRepositoryImpl(get(), get()) }
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

fun appDataModule(mockAlarms: Boolean, mockRingtones: Boolean) = module {
    single<AlarmsRepository>(Real) { provideAlarmsRepository(androidContext(), get()) }
    single<RingtoneRepository>(Real) { RingtoneRepositoryImpl(androidContext()) }

    if (AlarmMeApplication.IS_DEV_FLAVOR) {
        single<AlarmsRepository>(Mock) { MockAlarmsRepository() }
        single<RingtoneRepository>(Mock) { MockRingtoneRepository() }
    }

    factory<AlarmsRepository> {
        val useMock = AlarmMeApplication.IS_DEV_FLAVOR && mockAlarms
        if (useMock) get<AlarmsRepository>(Mock) else get<AlarmsRepository>(Real)
    }

    factory<RingtoneRepository> {
        val useMock = AlarmMeApplication.IS_DEV_FLAVOR && mockRingtones
        if (useMock) get<RingtoneRepository>(Mock) else get<RingtoneRepository>(Real)
    }
}

