package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.appSettings.AppSettingsRepository
import dev.alidroid.alarmme.feature.debugPanel.domain.ResetAppSettingsUseCase

/**
 * Clears the app settings store rather than re-writing each default, so the defaults stay
 * declared in one place — `AppSettingsRepositoryImpl`.
 */
class ResetAppSettingsUseCaseImpl(
    private val appSettingsRepository: AppSettingsRepository,
) : ResetAppSettingsUseCase {

    override suspend fun reset() = appSettingsRepository.clear()
}
