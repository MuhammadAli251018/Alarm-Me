package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.appSettings.AppInfoRepository
import dev.alidroid.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase

class AppBuildInfoUseCaseImpl(
    private val appInfoRepository: AppInfoRepository,
) : AppBuildInfoUseCase {

    override suspend fun getBuildInfo(): String {
        val appInfo = appInfoRepository.getAppInfo()
        return "v${appInfo.appVersion} · build ${appInfo.appVersionCode} · ${appInfo.gitHashName}"
    }
}
