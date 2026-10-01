package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase

/** Mocked: real info needs :app's BuildConfig, which feature:debug-panel can't depend on. */
class AppBuildInfoUseCaseImpl : AppBuildInfoUseCase {

    override suspend fun getBuildInfo(): String = "v1.0.0-debug · build 142 · a3f9c21"
}
