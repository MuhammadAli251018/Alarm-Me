package com.muhammadali.alarmme.feature.main.viewmodels.main

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepository
import com.muhammadali.alarmme.feature.main.domain.AlarmReceiver
import com.muhammadali.alarmme.feature.main.domain.AlarmScheduler
import com.muhammadali.alarmme.feature.main.ui.component.util.AlarmItemState
import com.muhammadali.alarmme.feature.main.ui.screen.util.Date
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time
import com.muhammadali.alarmme.feature.main.ui.screen.util.toMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/*
*   alarms: List<AlarmItemState>,
    onItemClick: (index: Int) -> Unit,
    onItemSwitchClick: (index: Int, isScheduled: Boolean) -> Unit,
    onAddBtnClick: () -> Unit
    * */

/*
* ringTime: Time,
    alarmTimeInit: Time,
    dateInit: Date,
    repeatInitialValue: Array<Boolean>,
    alarmTitle: String,
    ringtoneName: String,
    vibrationMode: String,
    onAlarmTimeClick: (Time) -> Unit,
    onDatePickClick: () -> Unit,
    onSoundPickerClick: () -> Unit,
    onVibrationPickerClick: () -> Unit,
    onSaveCancelClick: (save: Boolean) -> Unit
* */

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val alarmsDbRepository: AlarmsDbRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    //main screen states
    private val _alarms = alarmsDbRepository.getAllAlarms()
    val alarms = mutableStateOf(listOf<Alarm>())

    //data screen's initial state is the Create new alarm
    //alarm data screen states
    private val alarmTitle = mutableStateOf("Alarm")
    private val alarmTime = getCurrentLocalTime()
    private val alarmDate = getCurrentDate()
    private val alarmRepeat = arrayOf(true, true, true, true, true, true, true)
//    private val alarmVibrationMode =

    init {
        viewModelScope.launch {
            _alarms.collectLatest { newAlarms ->
                alarms.value = newAlarms
            }
        }
    }

    private fun getCurrentLocalTime(): Time = getTimeAsTimeFormat(System.currentTimeMillis())

    private fun getCurrentDate(): Date = getDateFormat(System.currentTimeMillis())

    private fun getTimeAsTimeFormat(timeInMillis: Long): Time {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val hours = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)

        return Time(hours, minutes)
    }

    private fun getDateFormat(timeInMillis: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return Date(year, month.toMonth(), day)
    }
    private fun changeDataScreenStateToCreateAlarm() {

    }

    private fun changeDataScreenStateToEdit() {

    }

    private fun isDataScreenInInitialState(): Boolean {
        //todo complete the compression
        return true
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
                alarmTime = "Todo" /*todo*/,
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
        /*todo complete*/
        return true
    }

    fun onAddBtnClick() {
        // todo open data-alarm-screen with new alarm data
    }

}