package com.muhammadali.alarmme.feature.main.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<Alarm>>

    /** the elements are put in ASC order */
    @Query("SELECT * FROM alarms WHERE scheduled = 1 ORDER BY `index` ASC")
    fun getScheduledAlarms(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms ORDER BY  time DESC")
    fun getAllAlarmsOrderedByTime(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE `index` = 0 AND scheduled = 1")
    fun getFirstAlarmToRing(): Flow<Alarm>

    @Query("SELECT * FROM alarms WHERE id = :id")
    fun getAlarmById(id: Int): Flow<Alarm>

    /**
     * elements must have the same id
     * */
    @Upsert
    suspend fun insertOrUpdateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}