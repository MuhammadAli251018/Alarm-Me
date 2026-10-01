package com.muhammadali.alarmme.feature.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsReadUseCase {
    fun observeSettings(): Flow<SettingsSnapshot>
}
