package com.muhammadali.alarmme.feature.main.ui.viewmodels.main

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepository
import com.muhammadali.alarmme.feature.main.domain.AlarmReceiver
import com.muhammadali.alarmme.feature.main.domain.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.TimeAdapter
import com.muhammadali.alarmme.feature.main.ui.component.util.AlarmItemState
import com.muhammadali.alarmme.feature.main.ui.util.getTimeState
import com.muhammadali.alarmme.feature.main.ui.util.getTwelveModeHours
import com.muhammadali.alarmme.feature.main.ui.util.toAnnotatedString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val alarmsDbRepository: AlarmsDbRepository,
    private val alarmScheduler: AlarmScheduler,
    private val timeAdapter: TimeAdapter
) : ViewModel() {

    //main screen states
    private val _alarms = alarmsDbRepository.getAllAlarms()
    val alarms = mutableStateOf(listOf<Alarm>())
    val alarmsStateList = mutableStateOf(listOf<AlarmItemState>())

    init {
        viewModelScope.launch {
            _alarms.collectLatest { newAlarms ->
                alarmsStateList.value = newAlarms.map { alarm ->
                    val time = timeAdapter.getTimeFormat(alarm.time)
                    val repeat = mutableListOf<Boolean>()
                    alarm.repeat.forEach {
                        if (it == '0')
                            repeat.add(true)
                        else
                            repeat.add(false)
                    }
                    AlarmItemState(
                        alarmTitle = alarm.title,
                        alarmTime = time.toAnnotatedString(
                            timeStyle = SpanStyle(fontSize = 32.sp),
                            periodStyle = SpanStyle(fontSize = 16.sp)
                        ),
                        alarmRepeat = repeat.toTypedArray(),
                        isScheduled = alarm.scheduled
                    )
                }
            }
        }
    }

    fun onItemClick(index: Int) {
        //todo open data-alarm-screen with alarm data

    }

    fun onItemSwitchBtnClick(index: Int, isScheduled: Boolean, context: Context) {
        //todo edit the alarm time in alarm manager & in DB
        val alarm = alarms.value[index]

        if (isScheduled) {
            alarmScheduler.setAlarm(
                time = alarm.time,
                context = context,
                receiver = AlarmReceiver::class.java,
                alarmDBId = alarm.id,
                alarmTitle = alarm.title,
                alarmTime = getAlarmTimeInStringFormat(alarm.time),
                alarmSoundUri = alarm.ringtoneRef,
                alarmVibration = alarm.vibration,
                alarmSnooze = getIsSnoozeEnabled(alarm.snooze)
            )
        }
        else {
            alarmScheduler.cancelAlarm(
                context = context,
                receiver = AlarmReceiver::class.java
            )
        }

        viewModelScope.launch (Dispatchers.IO) {
            alarmsDbRepository.insertOrUpdateAlarm(alarm.copy(scheduled = !alarm.scheduled))
        }
    }

    private fun getIsSnoozeEnabled(string: String): Boolean {
        return string == "true"
    }

    private fun getAlarmTimeInStringFormat(time: Long): String {
        val localTime = timeAdapter.getTimeFormat(time)

        return "${localTime.getTwelveModeHours()}:${localTime.minute}" +
                " ${localTime.getTimeState()}"
    }

    fun onAddBtnClick() {
        // todo open data-alarm-screen with new alarm data
    }

}