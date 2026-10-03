package dev.alidroid.alarmme.feature.debugPanel.domain

interface AppBuildInfoUseCase {
    suspend fun getBuildInfo(): String
}
