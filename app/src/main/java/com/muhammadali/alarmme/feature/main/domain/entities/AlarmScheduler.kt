package com.muhammadali.alarmme.feature.main.domain.entities

import com.muhammadali.alarmme.common.util.Result

interface AlarmScheduler {
    fun scheduleOrUpdate(alarm: Alarm): Result<Unit>

    fun cancelAlarm(alarmId: Int): Result<Unit>
}