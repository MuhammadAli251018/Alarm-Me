package com.muhammadali.alarmme.feature.main.data.repo

import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.AlarmsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DBRepoImp @Inject constructor(
    override val alarmsDao: AlarmsDao
) : DBRepository {

    override suspend fun insertOrUpdateAlarm(alarm: Alarm) {
        alarmsDao.insertOrUpdateAlarm(alarm)
        arrangeDB()
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmsDao.deleteAlarm(alarm)
        arrangeDB()
    }

    private fun getAlarmsToUpdate(alarms: List<Alarm>): List<Alarm> {
        val sortedAlarms = alarms.sortedBy {
            it.time
        }

        val alarmsToUpdate = mutableListOf<Alarm>()

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

    override fun getAllAlarms(): Flow<List<Alarm>> = alarmsDao.getAllAlarms()

    override fun getScheduledAlarm(): Flow<List<Alarm>> = alarmsDao.getScheduledAlarms()

    override fun getAllAlarmsOrderedByTime(): Flow<List<Alarm>> = alarmsDao.getAllAlarmsOrderedByTime()

    override fun getFirstAlarmToRing(): Flow<Alarm> = alarmsDao.getFirstAlarmToRing()

    override fun getAlarmById(id: Int): Flow<Alarm> = alarmsDao.getAlarmById(id)

}