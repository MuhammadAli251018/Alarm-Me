package dev.alidroid.alarmme.feature.debugPanel.domain

import kotlin.time.Duration


interface ScheduleAlarmUseCase {
    suspend fun fireDebugAlarmIn(duration: Duration)
}