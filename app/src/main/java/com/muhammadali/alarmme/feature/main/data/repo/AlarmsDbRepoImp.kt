package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.AlarmsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmsDbRepoImp @Inject constructor(
    private val alarmsDao: AlarmsDao
) : AlarmsDbRepository {

    override fun getAllAlarms(): Flow<List<Alarm>> = alarmsDao.getAllAlarms()

    override fun getScheduledAlarm(): Flow<List<Alarm>> = alarmsDao.getScheduledAlarm()

    override suspend fun insertOrUpdateAlarm(alarm: Alarm) = alarmsDao.insertOrUpdateAlarm(alarm)

    override suspend fun deleteAlarm(alarm: Alarm) = alarmsDao.deleteAlarm(alarm)
}