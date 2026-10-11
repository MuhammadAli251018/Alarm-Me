package dev.alidroid.alarmme.featureFlags

import kotlinx.coroutines.flow.Flow

interface FeatureFlagsRepository {
    fun observeGlobalMocks(): Flow<Boolean>
    suspend fun setGlobalMocks(enabled: Boolean)

    fun observeMockAlarms(): Flow<Boolean>
    suspend fun setMockAlarms(enabled: Boolean)

    fun observeMockRingtones(): Flow<Boolean>
    suspend fun setMockRingtones(enabled: Boolean)

    suspend fun clear()
}
