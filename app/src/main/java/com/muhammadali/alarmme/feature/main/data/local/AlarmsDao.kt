package com.muhammadali.alarmme.feature.main.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE id = :id")
    fun getAlarmById(id: Int): Flow<AlarmEntity>

    @Upsert
    fun insertOrUpdateAlarm(alarmEntity: AlarmEntity)

    @Query("DELETE FROM alarms WHERE id = :id")
    fun deleteAlarm(id: Int)
}