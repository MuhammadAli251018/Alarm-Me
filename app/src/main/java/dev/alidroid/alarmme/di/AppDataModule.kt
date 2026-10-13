package dev.alidroid.alarmme.di

import dev.alidroid.alarmme.AlarmMeApplication
import dev.alidroid.alarmme.AlarmReceiver
import dev.alidroid.alarmme.BuildConfig
import dev.alidroid.alarmme.alarms.data.MockAlarmsRepository
import dev.alidroid.alarmme.alarms.data.MockRingtoneRepository
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
import org.koin.dsl.module


val appDataModule = module {
    single<AlarmsRepository> { MockAlarmsRepository() }
    single<RingtoneRepository> { MockRingtoneRepository() }
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
