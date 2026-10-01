package com.muhammadali.alarmme.feature.debugPanel.domain

import kotlinx.coroutines.flow.Flow

interface FeatureFlagsReadUseCase {
    fun observeFlags(): Flow<FeatureFlagsSnapshot>
}
