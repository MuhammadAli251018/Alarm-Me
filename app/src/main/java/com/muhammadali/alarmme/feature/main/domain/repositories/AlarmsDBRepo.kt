package com.muhammadali.alarmme.feature.main.domain.repositories

import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm

interface AlarmsDBRepo {

    fun addOrUpdateAlarm(alarm: Alarm): Result<Unit>

    fun deleteAlarm(alarmId: String): Result<Unit>

    fun getAllAlarms(): Result<List<Alarm>>
}