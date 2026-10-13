package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.appSettings.AppInfoRepository
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfo
import dev.alidroid.alarmme.feature.settings.domain.AppVersionInfoUseCase

class AppVersionInfoUseCaseImpl(
    private val appInfoRepository: AppInfoRepository,
) : AppVersionInfoUseCase {

    override suspend fun getAppVersionInfo(): AppVersionInfo {
        val appInfo = appInfoRepository.getAppInfo()
        return AppVersionInfo(
            versionLabel = "${appInfo.appVersion} (${appInfo.appVersionCode}) · ${appInfo.gitHashName}",
            showDeveloperSection = true
        )
    }
}
