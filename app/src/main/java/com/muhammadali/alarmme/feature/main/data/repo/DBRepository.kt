package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.feature.main.data.local.AlarmEntity
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDao
import kotlinx.coroutines.flow.Flow

interface AlarmsDBRepo {
    fun arrangeDB()

    val alarmsDao: AlarmsDao

    fun getAllAlarms(): Flow<List<AlarmEntity>>

    fun getScheduledAlarm(): Flow<List<AlarmEntity>>

    fun getAllAlarmsOrderedByTime(): Flow<List<AlarmEntity>>

    fun getFirstAlarmToRing(): Flow<AlarmEntity>

    fun getAlarmById(id: Int): Flow<AlarmEntity>

    suspend fun insertOrUpdateAlarm(alarmEntity: AlarmEntity) /**/

    suspend fun deleteAlarm(alarmEntity: AlarmEntity) /*= alarmsDao.deleteAlarm(alarm)*/
}