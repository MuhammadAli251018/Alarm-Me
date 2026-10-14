package dev.alidroid.alarmme.featureFlags

import kotlinx.coroutines.flow.StateFlow

interface FeatureFlagsRepository {
    fun observeGlobalMocks(): StateFlow<Boolean>
    suspend fun setGlobalMocks(enabled: Boolean)

    fun observeMockAlarms(): StateFlow<Boolean>
    suspend fun setMockAlarms(enabled: Boolean)

    fun observeMockRingtones(): StateFlow<Boolean>
    suspend fun setMockRingtones(enabled: Boolean)

    suspend fun clear()
}
