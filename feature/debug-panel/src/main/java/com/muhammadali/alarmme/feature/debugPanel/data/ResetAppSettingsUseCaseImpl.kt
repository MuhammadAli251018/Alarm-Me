package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.feature.debugPanel.domain.ResetAppSettingsUseCase
import com.muhammadali.alarmme.preferences.PreferencesRepository

/**
 * Clears the app settings store rather than re-writing each default, so the defaults stay
 * declared in one place — `AppSettings`.
 */
class ResetAppSettingsUseCaseImpl(
    private val appSettingsPreferences: PreferencesRepository,
) : ResetAppSettingsUseCase {

    override suspend fun reset() = appSettingsPreferences.clear()
}
