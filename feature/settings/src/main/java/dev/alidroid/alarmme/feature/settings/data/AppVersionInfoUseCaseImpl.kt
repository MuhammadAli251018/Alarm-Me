package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfo
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfoUseCase

/** Mocked: real info needs :app's BuildConfig, which feature:settings can't depend on. */
class AppVersionInfoUseCaseImpl : AppVersionInfoUseCase {

    override suspend fun getAppVersionInfo(): AppVersionInfo = AppVersionInfo(
        versionLabel = "1.0.0",
        showDeveloperSection = true
    )
}
