package com.muhammadali.alarmme.feature.main.ui.viewmodels.main

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepository
import com.muhammadali.alarmme.feature.main.domain.AlarmReceiver
import com.muhammadali.alarmme.feature.main.domain.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.TimeAdapter
import com.muhammadali.alarmme.feature.main.ui.util.minus
import com.muhammadali.alarmme.feature.main.ui.util.toAnnotatedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


class AlarmDataScreenVM @Inject constructor(
    private val alarmsDbRepository: AlarmsDbRepository,
    private val alarmScheduler: AlarmScheduler,
    private val timeAdapter: TimeAdapter
) : ViewModel() {

    val alarmTime = mutableStateOf(LocalTime.now().plusHours(1))
    val ringingTime = mutableStateOf(getRingingTime(alarmTime.value))
    val repeatingPattern = mutableStateOf(arrayOf(true, true, true, true, true, true, true))
    val alarmTitle = mutableStateOf("Alarm")
    val date = mutableStateOf(LocalDate.now())
    val ringtoneName = mutableStateOf("Default")
    val vibrationMode = mutableStateOf(true)
    val snoozeMode = mutableStateOf(true)
    private var soundUri: Uri? = null

    private fun  getRingingTime(alarmTime: LocalTime): LocalTime = alarmTime minus LocalTime.now()

    fun onAlarmTimePick(time: LocalTime) {
        alarmTime.value = time
    }

    fun onAlarmDatePick(date: LocalDate) {
        this.date.value = date
    }

    fun onSoundPickResult(uri: Uri) {
        soundUri = uri
        //todo ringtoneName = uri.getName()
    }

    fun onVibrationPickerClick(vibration: Boolean) {
        vibrationMode.value = vibration
    }

    fun onSoundPickerClick() {
        //todo
    }

    fun onSnoozePickerClick(snooze: Boolean) {
        snoozeMode.value = snooze
    }

    fun onSaveBtnClick(save: Boolean, context: Context) {
        if (!save)
            return

        val time = timeAdapter.getTimeInMillis(LocalDateTime.of(
            date.value.year,
            date.value.month,
            date.value.dayOfMonth,
            alarmTime.value.hour,
            alarmTime.value.minute
        ))

        var repeat = ""

        repeatingPattern.value.forEach {
            repeat += it.toString()
        }

        viewModelScope.launch(Dispatchers.IO) {

            var alarms = mutableListOf<Alarm>()
            var index = 0

            alarmsDbRepository.getAllAlarms().collectLatest {alarmList ->
                alarms = alarmList.filter {alarm ->
                    alarm.scheduled
                }.sortedByDescending {alarm ->
                    alarm.time
                }.toMutableList()

                if (alarms.isEmpty())
                    index = 0

                val iteratorAlarm = alarms


                for (i in 0 .. iteratorAlarm.lastIndex ){
                    val alarm = iteratorAlarm[i]
                    var isReIndexed = false

                    if (time < alarm.time) {
                        index = alarm.index

                        for (j in i .. iteratorAlarm.lastIndex) {
                            iteratorAlarm[i] = alarms[i].copy(index = alarms[i].index + 1)
                            isReIndexed = true
                        }

                    }

                    if (isReIndexed)
                        break
                }

                alarms = iteratorAlarm
            }


            val alarm = Alarm(
                time = time,
                title = alarmTitle.value,
                scheduled = true,
                repeat = repeat,
                vibration = vibrationMode.value,
                ringtoneRef = soundUri.toString(),
                snooze = snoozeMode.value.toString(),
                index = index
            )

            alarms.add(alarm)

            alarms.forEach {updatedAlarm ->
                alarmsDbRepository.insertOrUpdateAlarm(updatedAlarm)
            }

            val nextAlarmTime = timeAdapter.getTimeFormat(alarms[0].time)
            alarmScheduler.setAlarm(
                time = alarms[0].time,
                context = context,
                receiver = AlarmReceiver::class.java,
                alarmDBId = alarms[0].id,
                alarmTitle = alarms[0].title,
                alarmTime = nextAlarmTime.toAnnotatedString().toString(),
                alarmSoundUri = alarms[0].toString(),
                alarmVibration = alarms[0].vibration,
                alarmSnooze = alarms[0].snooze == "true"
            )
        }
    }

}