package dev.alidroid.alarmme.feature.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsReadUseCase {
    fun observeSettings(): Flow<SettingsSnapshot>
}
