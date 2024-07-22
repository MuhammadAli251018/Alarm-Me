package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.AlarmsDao
import kotlinx.coroutines.flow.Flow

interface AlarmsDbRepository {

    fun getAllAlarms(): Flow<List<Alarm>>

    /** the elements are put in ASC order */
    fun getScheduledAlarm(): Flow<List<Alarm>>

    /**
     * elements must have the same id
     * */
    suspend fun insertOrUpdateAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)
}