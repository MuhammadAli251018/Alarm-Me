package com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.data.local.AlarmEntity
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmPreferences
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.AlarmDataScreenPreview
import com.muhammadali.alarmme.feature.main.presentaion.util.Ringtone
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import javax.inject.Inject

private const val TAG = "alarm_data_view_model"

@HiltViewModel
class AlarmDataScreenVM @Inject constructor(
    private val dbRepository: AlarmsDBRepo,
    private val alarmScheduler: AlarmScheduler,
    private val timeDateFormatter: TimeDateFormatter,
    private val timeAdapter: TimeAdapter
) : ViewModel() , DataScreenPresenter {

    private var alarmEntity: AlarmEntity = getDefaultAlarm()

    private fun getDefaultAlarm(): AlarmEntity {
        return AlarmEntity(
            time = System.currentTimeMillis(),
            title = "Alarm",
            scheduled = true,
            repeat = AlarmPreferences.RepeatPattern.Weekly(setOf(AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Monday)).toString(),
            vibration = true,
            ringtoneRef = "", //todo add default ringtone to assets
            snooze = 0
        )
    }

    override fun loadAlarmById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.getAlarmWithId(id).collectLatest {result ->
                result.handleData(
                    onSuccess = {alarm ->
                                //  todo update ui state
                    },
                    onFailure = {/*todo*/}
                )
            }
        }
    }


    /*private fun saveAlarmData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            dbRepository.addOrUpdateAlarm(TODO())
        }

        alarmScheduler.scheduleOrUpdate(TODO())
    }

    private fun getRingingTime(alarmTime: LocalTime): LocalTime {
        return TODO()
    }*/

    //private fun  getRingingTime(alarmTime: LocalTime): LocalTime = alarmTime minus LocalTime.now()

    /*override fun getLocalTime(hours: Int, minutes: Int): LocalTime {
        val time = LocalDateTime.of(
            alarmLocalTime.year,
            alarmLocalTime.month.value,
            alarmLocalTime.dayOfMonth,
            hours,
            minutes
        )

        alarmEntity = alarmEntity.copy(time = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        alarmLocalTime = time

        return time.toLocalTime()
    }*/

    /*override fun getLocalDate(year: Int, month: Month, dayOfMonth: Int): LocalTime {
        val time = LocalDateTime.of(
            year,
            month,
            dayOfMonth,
            alarmLocalTime.hour,
            alarmLocalTime.minute
        )

        alarmEntity = alarmEntity.copy(time = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        alarmLocalTime = time

        return time.toLocalTime()
    }*/

    /*private fun saveNewRingtoneUri(uri: Uri) {
        TODO()
        //alarmEntity = alarmEntity.copy(ringtoneRef = uri.toString())
    }*/

    /*override fun changeVibrationMode() {
        alarmEntity = alarmEntity.copy(vibration = !alarmEntity.vibration)
    }*/

    /*override fun changeSnoozeMode() {
        val snoozeMode = alarmEntity.snooze.toBooleanStrict()
        alarmEntity = alarmEntity.copy(snooze = (!snoozeMode).toString())
    }*/

    private val _ringingTime = MutableStateFlow(AlarmDataScreenPreview.ringTime)
    private val _alarmTime = MutableStateFlow(AlarmDataScreenPreview.alarmTime)
    private val _date = MutableStateFlow(AlarmDataScreenPreview.date)
    private val _repeatDays = MutableStateFlow(AlarmDataScreenPreview.repeat.toList())
    private val _alarmTitle = MutableStateFlow(AlarmDataScreenPreview.alarmTitle)
    private val _snoozeMod = MutableStateFlow(AlarmDataScreenPreview.snoozeMode)
    private val _ringtoneName = MutableStateFlow(AlarmDataScreenPreview.ringtoneName)
    private val _vibrationMode = MutableStateFlow(AlarmDataScreenPreview.vibrationMode)

    private val localDate = MutableStateFlow(LocalDate.now())
    private val localTime = MutableStateFlow(LocalTime.now())
    private val alarmScheduleTime = MutableStateFlow(LocalDateTime.now())

    override val ringingTime: StateFlow<AnnotatedString> = _ringingTime.asStateFlow()
    override val alarmTime: StateFlow<AnnotatedString> = _alarmTime.asStateFlow()
    override val date: StateFlow<String> = _date.asStateFlow()
    override val repeatDays: StateFlow<List<Boolean>> = _repeatDays.asStateFlow()
    override val alarmTitle: StateFlow<String> = _alarmTitle.asStateFlow()
    override val snoozeMode: StateFlow<String> = _snoozeMod.asStateFlow()
    override val ringtoneName: StateFlow<String> = _ringtoneName.asStateFlow()
    override val vibrationMode: StateFlow<String> = _vibrationMode.asStateFlow()


    override val onAlarmTimePick: (hour: Int, minute: Int) -> Unit = {hour, minute ->
        viewModelScope.launch(Dispatchers.Default) {
            alarmScheduleTime.emit(LocalDateTime.of(localDate.value, LocalTime.of(hour, minute)))
        }
    }
    override val onDayRepeatPick: (Int) -> Unit = {index ->

    }
    override val onAlarmTitleChange: (String) -> Unit = {newTitle ->

    }
    override val onDatePickerPick: (LocalDate) -> Unit = {newDate ->
        viewModelScope.launch(Dispatchers.Default) {
            alarmScheduleTime.emit(LocalDateTime.of(newDate, localTime.value))
        }
    }
    override val onSoundPickerClick: () -> Unit = {
        // Todo
    }
    override val onSoundPickResult: (Ringtone?) -> Unit = {
        // Todo
    }
    override val onVibrationPickerClick: () -> Unit = {
        // Todo
    }
    override val onSnoozePickerClick: () -> Unit = {
        // Todo
    }
    override val onSaveClick: (Context) -> Unit = {

        viewModelScope.launch (Dispatchers.IO){
            val alarm = Alarm(
                alarmId = -1,
                title = alarmTitle.value,
                time = timeAdapter.getTimeInMillis(alarmScheduleTime.value),
                // Todo fix preferences
                preferences = AlarmPreferences(
                    AlarmPreferences.Snooze.NoSnooze,
                    vibration = false,
                    ringtoneName.value,
                    AlarmPreferences.RepeatPattern.Weekly(setOf(AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Monday))
                ),
                enabled = true
            )

            dbRepository.addOrUpdateAlarm(
                alarm
            )

            alarmScheduler.scheduleOrUpdate(alarm)
        }

    }
    override val onCancelClick: (Context) -> Unit = {

    }
}