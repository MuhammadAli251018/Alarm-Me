package dev.alidroid.alarmme.feature.settings.domain

data class AppVersionInfo(
    val versionLabel: String,
    val showDeveloperSection: Boolean,
)

interface AppVersionInfoUseCase {
    suspend fun getAppVersionInfo(): AppVersionInfo
}
