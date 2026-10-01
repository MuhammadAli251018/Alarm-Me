package com.muhammadali.alarmme.feature.debugPanel.domain

interface FeatureFlagsUpdateUseCase {
    suspend fun setGlobalMocks(enabled: Boolean)
    suspend fun setMockAlarms(enabled: Boolean)
    suspend fun setMockRingtones(enabled: Boolean)
    suspend fun resetAllFlags()
}
