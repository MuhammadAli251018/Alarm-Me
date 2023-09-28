package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.AlarmsDao
import kotlinx.coroutines.flow.Flow

interface AlarmsDBRepo {
    fun arrangeDB()

    val alarmsDao: AlarmsDao

    fun getAllAlarms(): Flow<List<Alarm>>

    fun getScheduledAlarm(): Flow<List<Alarm>>

    fun getAllAlarmsOrderedByTime(): Flow<List<Alarm>>

    fun getFirstAlarmToRing(): Flow<Alarm>

    fun getAlarmById(id: Int): Flow<Alarm>

    suspend fun insertOrUpdateAlarm(alarm: Alarm) /**/

    suspend fun deleteAlarm(alarm: Alarm) /*= alarmsDao.deleteAlarm(alarm)*/
}