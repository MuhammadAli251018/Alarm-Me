package dev.alidroid.alarmme.featureFlags

import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository.Companion.DefaultTimeoutDuration
import dev.alidroid.alarmme.preferences.PreferencesValue
import dev.alidroid.alarmme.preferences.awaitFirstRealValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface FeatureFlagsRepository {

    companion object {
        val DefaultTimeoutDuration = 5.minutes
    }

    fun isDebugPanelEnabled(): Boolean

    fun observeGlobalMocks(): PreferencesValue<Boolean>
    suspend fun setGlobalMocks(enabled: Boolean)

    fun observeMockAlarms(): PreferencesValue<Boolean>
    suspend fun setMockAlarms(enabled: Boolean)

    fun observeMockRingtones(): PreferencesValue<Boolean>
    suspend fun setMockRingtones(enabled: Boolean)

    suspend fun clear()
}


suspend fun FeatureFlagsRepository.awaitGlobalMocks(timeout: Duration = DefaultTimeoutDuration): Boolean =
    observeGlobalMocks().awaitFirstRealValue(timeout)

suspend fun FeatureFlagsRepository.awaitMockAlarms(timeout: Duration = DefaultTimeoutDuration): Boolean =
    observeMockAlarms().awaitFirstRealValue(timeout)

suspend fun FeatureFlagsRepository.awaitMockRingtones(timeout: Duration = DefaultTimeoutDuration): Boolean =
    observeMockRingtones().awaitFirstRealValue(timeout)