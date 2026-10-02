package com.muhammadali.alarmme.feature.debugPanel.domain

import kotlin.time.Duration


interface ScheduleAlarmUseCase {
    suspend fun fireDebugAlarmIn(duration: Duration)
}