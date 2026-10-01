package com.muhammadali.alarmme.feature.debugPanel.domain

interface AppBuildInfoUseCase {
    suspend fun getBuildInfo(): String
}
