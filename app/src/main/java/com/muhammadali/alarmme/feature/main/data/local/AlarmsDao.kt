package com.muhammadali.alarmme.feature.main.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    /** the elements are put in ASC order */
    /*@Query("SELECT * FROM alarms WHERE scheduled = 1 ORDER BY `id` ASC")
    fun getScheduledAlarms(): Flow<List<AlarmEntity>>*/

    /*@Query("SELECT * FROM alarms ORDER BY  time DESC")
    fun getAllAlarmsOrderedByTime(): Flow<List<AlarmEntity>>*/

    /*
    @Query("SELECT * FROM alarms WHERE `index` = 0 AND scheduled = 1")
    fun getFirstAlarmToRing(): Flow<AlarmEntity>*/

    @Query("SELECT * FROM alarms WHERE id = :id")
    fun getAlarmById(id: Int): Flow<AlarmEntity>

    @Upsert
    suspend fun insertOrUpdateAlarm(alarmEntity: AlarmEntity)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarm(id: Int)
}