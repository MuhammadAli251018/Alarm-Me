package com.muhammadali.alarmme.feature.main.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** only to compare different properties a cross the test cases */
private infix fun Alarm.sameAS(alarm: Alarm): Boolean {
    return this.scheduled == alarm.scheduled
            && this.vibration == alarm.vibration
            && this.time == this.time
            && this.snooze == this.snooze
            && this.index == this.index
}

private infix fun List<Alarm>.sameAs(alarms: List<Alarm>): Boolean {
    if (size != alarms.size)
        return false

    for (index in 0 .. lastIndex)
        if (!(this[index] sameAS  alarms[index]))
            return false

    return true
}

@RunWith(JUnit4::class)
@SmallTest
class AlarmDataBaseTest {
    private lateinit var alarmsDB: AlarmsDB
    private lateinit var alarmDao: AlarmsDao
    private lateinit var alarm: Alarm
    private lateinit var alarms: MutableList<Alarm>

    @Before
    fun setupDB () {
        alarmsDB = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            AlarmsDB::class.java
        )
            .allowMainThreadQueries()
            .build()

        alarmDao = alarmsDB.alarmsDao()

        alarm = Alarm(
            time = System.currentTimeMillis(),
            title = "test alarm ",
            true,
            repeat = "0000000",
            vibration = true,
            ringtoneRef = "ringtone uri test",
            snooze = "true",
            index = 0,
            id = 1
        )

        alarms = mutableListOf()

        for (i in 0 .. 10)
            alarms.add(alarm.copy(
                id = i +1,
                index = i,
                scheduled = i % 2 == 0
            ))
    }

    @After
    fun terminateDB() {
        alarmsDB.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAlarm_returnTrue() = runTest {
        alarmDao.insertOrUpdateAlarm(alarm)
        alarmDao.getAllAlarms().test {
            val alarms = awaitItem()
            println("collected")
            assertThat(alarms).contains(alarm)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarmByGivenId_returnTrue() = runTest {
        alarms.forEach {
            alarmDao.insertOrUpdateAlarm(it)
        }

        alarmDao.getAlarmById(alarm.id).test {
            assertThat(awaitItem() sameAS alarm).isTrue()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarmsOrderedByTime_returnTrue() = runTest {
        alarms.forEach {
            alarmDao.insertOrUpdateAlarm(it)
        }

        alarmDao.getAllAlarmsOrderedByTime().test {
            val tesAlarms = alarms

            tesAlarms.sortByDescending { it.time }
            assertThat(awaitItem() sameAs tesAlarms)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllScheduledAlarms_returnTrue() = runTest {
        alarms.forEach {
            alarmDao.insertOrUpdateAlarm(it)
        }

        alarmDao.getScheduledAlarms().test {
            assertThat(awaitItem() sameAs alarms.filter { it.scheduled })
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarmWithIndexIsZero() = runTest {
        alarms.forEach {
            alarmDao.insertOrUpdateAlarm(it)
        }

        alarmDao.getAllAlarms().test {
            println(awaitItem())
        }

        alarmDao.getFirstAlarmToRing().test {
            assertThat(awaitItem() sameAS alarm)
        }
    }
}
