package com.muhammadali.alarmme.feature.debugPanel.domain

interface AlarmsWriteUseCase {
    suspend fun flushAlarms()
    suspend fun writeMockAlarms(alarmsCount: Int)
}