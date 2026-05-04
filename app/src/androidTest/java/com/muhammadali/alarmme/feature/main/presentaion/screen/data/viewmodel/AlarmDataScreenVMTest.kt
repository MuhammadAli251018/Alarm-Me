package com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel

import android.app.AlarmManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.muhammadali.alarmme.common.util.Failure
import com.muhammadali.alarmme.common.util.Result
import com.muhammadali.alarmme.common.util.Success
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmPreferences
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmSchedulerImp
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.LocalTime

private const val TAG = "AlarmDataScreenVMTestTAG"

/** Return all the indexes of items which makes checker returns true*/
fun <T> List<T>.contains(checker: (T) -> Boolean): List<Int> {
    val indexes = mutableListOf<Int>()

    for (i in indices)
        if (checker(this[i]))
            indexes.add(i)

    return indexes.toList()
}

fun <T> List<T>.contains(returnFist: Boolean = false, checker: (T) -> Boolean): List<Int> {
    val result = contains(checker)

    return if (returnFist && result.isNotEmpty())
        listOf(result.first())
    else
        result
}

class AlarmDataScreenVMTest{
    // TODO test view model

    lateinit var alarmDAtaScreenVM: AlarmDataScreenVM


    private class FakeDBRepo : AlarmsDBRepo {

        private val alarms = mutableListOf<Alarm>()


        override suspend fun addOrUpdateAlarm(alarm: Alarm): Result<Unit> {
            val foundAlarms = alarms.contains(returnFist = true){
                it.alarmId == alarm.alarmId
            }

            if (foundAlarms.isNotEmpty())
                alarms[foundAlarms.first()] = alarm
            else
                alarms.add(alarm)

            return Result.success(Unit)

        }

        override suspend fun deleteAlarm(alarmId: Int): Result<Unit> {
            val foundAlarms = alarms.contains(returnFist = true){
                it.alarmId == alarmId
            }

            return if (foundAlarms.isNotEmpty()) {
                alarms.remove(alarms[foundAlarms.first()])
                Result.success(Unit)
            }
            else
                Result.failure(Exception("Couldn't find alarm to delete"))
        }

        override fun getAllAlarms(): Flow<Result<List<Alarm>>> {
            return flow {
                emit(Result.success(alarms))
            }
        }

        override fun getAlarmWithId(id: Int): Flow<Result<Alarm>> {
            val foundAlarms = alarms.contains(returnFist = true){
                it.alarmId == id
            }

            return flow {
                if (foundAlarms.isNotEmpty())
                    emit(Result.success(alarms[foundAlarms.first()]))
                else
                    emit(Result.failure(Exception("Couldn't find alarm to delete")))
            }
        }
    }

    private class FakeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                Log.d(TAG, "received alarmId: ${intent.getStringExtra(AlarmSchedulerImp.AlARM_ID_KEY)}")
            }
        }
    }

    /*private class FakeService : Service() {
        override fun onBind(intent: Intent?): IBinder? {

            Log.d(TAG, "Hi from service")
            return null
        }

    }*/


    /*private fun runTest(test: (AlarmDataScreenVM) -> Unit) = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbRepository = FakeDBRepo()
        val alarmScheduler = AlarmSchedulerImp(
            context = context,
            receiver = FakeReceiver::class.java
        )

        alarmDAtaScreenVM = AlarmDataScreenVM(
            dbRepository = dbRepository,
            alarmScheduler = alarmScheduler,
            timeDateFormatter = TimeDateFormatter(),
            timeAdapter = TimeAdapterImp()
        )

        test(alarmDAtaScreenVM)
    }*/
    private fun runTest(test: (AlarmScheduler) -> Unit) = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val alarmScheduler = AlarmSchedulerImp(
            context = context,
            receiver = FakeReceiver::class.java
        )

        test(alarmScheduler)
    }

    /*@Test
    fun success_when_schedule_alarm() = runTest {viewModel: AlarmDataScreenVM ->

        val dateTime = LocalDateTime.now().plusMinutes(2)

        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext
        viewModel.onAlarmTimePick(dateTime.hour, dateTime.minute)
        viewModel.onDatePickerPick(dateTime.toLocalDate())

        viewModel.onSaveClick(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val nextAlarm = alarmManager.nextAlarmClock

        assert(nextAlarm.triggerTime == TimeAdapterImp().getTimeInMillis(dateTime) && nextAlarm.showIntent.isBroadcast)


    }*/

    @Test
    fun success_when_schedule_alarm() = runTest { alarmScheduler: AlarmScheduler ->
        val testAlarm = Alarm(
            alarmId = 1,
            time = TimeAdapterImp().getTimeInMillis(LocalDateTime.now().plusMinutes(2)),
            title = "testAlarm",
            enabled = true,
            preferences = AlarmPreferences(
                snooze = false,
                vibration = false,
                ringtone = AlarmPreferences.AlarmRingtone("null", "null"),
                repeat = AlarmPreferences.RepeatPattern.repeatOff()
            )
        )

        val result = alarmScheduler.scheduleOrUpdate(testAlarm)

        if (result is Failure)
            Log.e(TAG, "failed to schedule alarm | Exception: ${result.exception.message} | ${result.exception.stackTrace}")
        else

        assert(result is Success)
    }


}