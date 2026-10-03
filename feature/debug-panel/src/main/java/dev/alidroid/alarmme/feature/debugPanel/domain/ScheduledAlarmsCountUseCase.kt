package dev.alidroid.alarmme.feature.debugPanel.domain

import kotlinx.coroutines.flow.Flow

interface ScheduledAlarmsCountUseCase {
    fun observeScheduledAlarmsCount(): Flow<Int>
}
