package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDao
import com.muhammadali.alarmme.feature.main.data.local.toAlarm
import com.muhammadali.alarmme.feature.main.data.local.toAlarmEntity
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class AlarmsDbRepoImp(
    private val dbDao: AlarmsDao
) : AlarmsDBRepo {

    override suspend fun addOrUpdateAlarm(alarm: Alarm): Result<Unit> {
        return try {
            dbDao.insertOrUpdateAlarm(alarm.toAlarmEntity())
            Result.success(Unit)
        }
        catch (e: IOException) {
            Result.failure(Exception(""/*Todo: Handle*/))
        }
    }

    override suspend fun deleteAlarm(alarmId: Int): Result<Unit> {
        return try {
            dbDao.deleteAlarm(alarmId)
            Result.success(Unit)
        }
        catch (e: IOException) {
            Result.failure(Exception(""/*Todo: Handle*/))
        }
    }

    override fun getAllAlarms(): Flow<Result<List<Alarm>>> {
        return flow {
            try {
                val result= dbDao.getAllAlarms()

                result.collect { collectedAlarms ->

                    val alarms = mutableListOf<Alarm>()

                    collectedAlarms.forEach {
                        alarms.add(it.toAlarm())
                    }
                    this.emit(Result.success(alarms))
                }
            } catch (e: IOException) {
                emit(Result.failure(Exception(""/*Todo: Handle*/)))
            }
        }
    }

    override fun getAlarmWithId(id: Int): Flow<Result<Alarm>> {
        return flow {
            try {
                val result= dbDao.getAlarmById(id)

                result.collect { collectedAlarm ->
                    this.emit(Result.success(collectedAlarm.toAlarm()))
                }
            } catch (e: IOException) {
                emit(Result.failure(Exception(""/*Todo: Handle*/)))
            }
        }
    }
}