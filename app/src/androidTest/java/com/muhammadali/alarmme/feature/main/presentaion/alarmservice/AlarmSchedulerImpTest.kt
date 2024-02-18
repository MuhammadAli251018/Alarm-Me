package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmPreferences
import com.muhammadali.alarmme.feature.main.domain.entities.DaysOfWeeks
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class AlarmSchedulerImpTest {

    private lateinit var context: Context
    private lateinit var schedulerImp: AlarmSchedulerImp

    private class TestReceiver (
        val onReceiveAlarm: (Intent?) -> Unit
    ): BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onReceiveAlarm(intent)
        }

    }
    @Test
    fun test()  = runBlocking {

        context = ApplicationProvider.getApplicationContext()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        schedulerImp = AlarmSchedulerImp(TestReceiver::class.java, context)

        val alarm = Alarm(
            title = "TestAlarmTitle",
            time = System.currentTimeMillis() + 2.seconds.inWholeMilliseconds,
            alarmId = 0,
            preferences = AlarmPreferences(
                false,
                vibration = false,
                ringtoneRef = "Bla",
                repeat = AlarmPreferences.RepeatPattern.Weekly(setOf(DaysOfWeeks.Monday))
            ),
            enabled = true
        )
        schedulerImp.scheduleOrUpdate(alarm).handleData({
            val nextAlarm = alarmManager.nextAlarmClock


        }) {

        }


    }
}