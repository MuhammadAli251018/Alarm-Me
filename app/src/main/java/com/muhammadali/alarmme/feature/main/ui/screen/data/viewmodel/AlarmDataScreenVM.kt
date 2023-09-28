package com.muhammadali.alarmme.feature.main.ui.screen.data.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.data.repo.DBRepository
import com.muhammadali.alarmme.feature.main.domain.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.TimeAdapter
import com.muhammadali.alarmme.feature.main.ui.util.TimeDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import javax.inject.Inject

private const val TAG = "alarm_data_view_model"

@HiltViewModel
class AlarmDataScreenVM @Inject constructor(
    private val dbRepository: DBRepository,
    private val alarmScheduler: AlarmScheduler,
    override val timeAdapter: TimeAdapter,
    override val timeDateFormatter: TimeDateFormatter
) : ViewModel(), DataUIManager {

    private var alarm: Alarm = getDefaultAlarm()
    private val _uiState by lazy {  MutableStateFlow(alarmToDataUIState(alarm)) }
    override val uiState: StateFlow<DataUIState> by lazy { _uiState.asStateFlow() }
    private var alarmLocalTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(alarm.time), ZoneId.systemDefault())


    private fun getDefaultAlarm(): Alarm {
        return Alarm(
            time = System.currentTimeMillis(),
            title = "Alarm",
            true,
            repeat = AlarmRepeatingPattern().toString(),
            vibration = true,
            ringtoneRef = "", //todo add default ringtone to assets
            snooze = true.toString(),
            index = 0
        )
    }

    fun loadAlarmById(id: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            dbRepository.getAlarmById(id).collectLatest {
                alarm = it
            }
        }
    }

    override fun updateUIState(newState: DataUIState) {
        _uiState.update { newState }
    }

    override fun saveAlarmData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            dbRepository.insertOrUpdateAlarm(alarm)
        }

        alarmScheduler.scheduler.schedule(alarm, context)
    }

    override fun getRingingTime(alarmTime: LocalTime): LocalTime {
        val time = LocalTime.now()

        return LocalTime.of(
            alarmTime.hour /*- time.hour*/,
            alarmTime.minute /*- time.minute*/
        )
    }

    //private fun  getRingingTime(alarmTime: LocalTime): LocalTime = alarmTime minus LocalTime.now()

    override fun getLocalTime(hours: Int, minutes: Int): LocalTime {
        val time = LocalDateTime.of(
            alarmLocalTime.year,
            alarmLocalTime.month.value,
            alarmLocalTime.dayOfMonth,
            hours,
            minutes
        )

        alarm = alarm.copy(time = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        alarmLocalTime = time

        return time.toLocalTime()
    }

    override fun getLocalDate(year: Int, month: Month, dayOfMonth: Int): LocalTime {
        val time = LocalDateTime.of(
            year,
            month,
            dayOfMonth,
            alarmLocalTime.hour,
            alarmLocalTime.minute
        )

        alarm = alarm.copy(time = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        alarmLocalTime = time

        return time.toLocalTime()
    }

    override fun saveNewRingtoneUri(uri: Uri) {
        alarm = alarm.copy(ringtoneRef = uri.toString())
    }

    override fun changeVibrationMode() {
        alarm = alarm.copy(vibration = !alarm.vibration)
    }

    override fun changeSnoozeMode() {
        val snoozeMode = alarm.snooze.toBooleanStrict()
        alarm = alarm.copy(snooze = (!snoozeMode).toString())
    }

}