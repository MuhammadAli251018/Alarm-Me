package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.feature.main.data.local.AlarmEntity
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DBRepoImp @Inject constructor(
    override val alarmsDao: AlarmsDao
) : DBRepository {

    override suspend fun insertOrUpdateAlarm(alarmEntity: AlarmEntity) {
        alarmsDao.insertOrUpdateAlarm(alarmEntity)
        arrangeDB()
    }

    override suspend fun deleteAlarm(alarmEntity: AlarmEntity) {
        alarmsDao.deleteAlarm(alarmEntity)
        arrangeDB()
    }

    private fun getAlarmsToUpdate(alarmEntities: List<AlarmEntity>): List<AlarmEntity> {
        val sortedAlarms = alarmEntities.sortedBy {
            it.time
        }

        val alarmsToUpdate = mutableListOf<AlarmEntity>()

        for (index in 0 .. sortedAlarms.lastIndex) {
            if (index != sortedAlarms[index].index) {
                alarmsToUpdate.add(sortedAlarms[index].copy(
                    index = index
                ))
            }
        }

        return alarmsToUpdate
    }

    override fun arrangeDB() {
        val workingScope = CoroutineScope(Dispatchers.IO)

        workingScope.launch {
            getScheduledAlarm().collectLatest { alarms ->
                if (alarms.isEmpty())
                    return@collectLatest

                val alarmsToUpdate = getAlarmsToUpdate(alarms)

                alarmsToUpdate.forEach {
                    insertOrUpdateAlarm(it)
                }
            }
        }
    }

    override fun getAllAlarms(): Flow<List<AlarmEntity>> = alarmsDao.getAllAlarms()

    override fun getScheduledAlarm(): Flow<List<AlarmEntity>> = alarmsDao.getScheduledAlarms()

    override fun getAllAlarmsOrderedByTime(): Flow<List<AlarmEntity>> = alarmsDao.getAllAlarmsOrderedByTime()

    override fun getFirstAlarmToRing(): Flow<AlarmEntity> = alarmsDao.getFirstAlarmToRing()

    override fun getAlarmById(id: Int): Flow<AlarmEntity> = alarmsDao.getAlarmById(id)

}