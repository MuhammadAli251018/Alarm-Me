package dev.alidroid.alarmme.feature.debugPanel.domain

interface ResolveExactAlarmPermissionUseCase {
    suspend fun isGranted(): Boolean
}
