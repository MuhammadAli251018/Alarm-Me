package dev.alidroid.alarmme.alarms.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms")
    fun observeAll(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE id = :id")
    fun observe(id: String): Flow<AlarmEntity?>

    @Upsert
    suspend fun insertOrUpdate(entity: AlarmEntity)

    @Query("UPDATE alarms SET enabled = :enabled WHERE id IN (:ids)")
    suspend fun setEnabled(ids: List<String>, enabled: Boolean)

    @Query("DELETE FROM alarms WHERE id IN (:ids)")
    suspend fun delete(ids: List<String>)

    @Query("DELETE FROM alarms")
    suspend fun deleteAll()
}
