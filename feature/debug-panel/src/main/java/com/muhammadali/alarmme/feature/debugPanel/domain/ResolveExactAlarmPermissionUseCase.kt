package com.muhammadali.alarmme.feature.debugPanel.domain

interface ResolveExactAlarmPermissionUseCase {
    suspend fun isGranted(): Boolean
}
