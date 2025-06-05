package com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.common.domain.AlarmPreferences
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.common.domain.DaysOfWeeks
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.common.domain.getFromIndex
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.AlarmDataScreenPreview
import com.muhammadali.alarmme.feature.main.presentaion.util.Ringtone
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import com.muhammadali.alarmme.feature.main.presentaion.util.toBooleanList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private const val TAG = "alarm_data_view_model"

class AlarmDataScreenVM (
    private val dbRepository: AlarmsDBRepo,
    private val alarmScheduler: AlarmScheduler,
    private val timeDateFormatter: TimeDateFormatter,
    private val timeAdapter: TimeAdapter
) : ViewModel() , DataScreenPresenter {

    private val _ringingTime = MutableStateFlow(AlarmDataScreenPreview.ringTime)
    private val _alarmTime = MutableStateFlow(AlarmDataScreenPreview.alarmTime)
    private val _date = MutableStateFlow(AlarmDataScreenPreview.date)
    private val _repeatDays = MutableStateFlow(AlarmDataScreenPreview.repeat)
    private val _alarmTitle = MutableStateFlow(AlarmDataScreenPreview.alarmTitle)
    private val _snoozeMod = MutableStateFlow(AlarmDataScreenPreview.snoozeMode)
    private val _ringtoneName = MutableStateFlow(AlarmDataScreenPreview.ringtoneName)
    private val _vibrationMode = MutableStateFlow(AlarmDataScreenPreview.vibrationMode)

    override val ringingTime: StateFlow<AnnotatedString> = _ringingTime.asStateFlow()
    override val alarmTime: StateFlow<AnnotatedString> = _alarmTime.asStateFlow()
    override val date: StateFlow<String> = _date.asStateFlow()
    override val repeatDays: StateFlow<List<Boolean>> = _repeatDays.asStateFlow()
    override val alarmTitle: StateFlow<String> = _alarmTitle.asStateFlow()
    override val snoozeMode: StateFlow<String> = _snoozeMod.asStateFlow()
    override val ringtoneName: StateFlow<String> = _ringtoneName.asStateFlow()
    override val vibrationMode: StateFlow<String> = _vibrationMode.asStateFlow()


    private var alarm  = getDefaultAlarm()
        set(value) {
            field = value
            updateUI()
        }

    private suspend fun <T> MutableStateFlow<T>.emitChangesOnly(newValue: T): Boolean {
        val changed = value != newValue

        if (changed)
            emit(newValue)

        return changed
    }


    private fun updateUI() {
        viewModelScope.launch {
            val ringingTime = (alarm.time - System.currentTimeMillis())
            val hours = ringingTime / (60 * 60 * 1000)
            val minutes = (ringingTime - (hours * 60 * 60 * 1000)).toDouble() / (1000 * 60)

            Log.d(TAG, "hours: ${ringingTime / (1000 * 60 * 60)}")
            _ringingTime
                .emitChangesOnly(timeDateFormatter.formatRingingTimeToAnnotatedString(hours.toInt(), minutes.toInt()))

            _alarmTime
                .emitChangesOnly(timeDateFormatter.formatAlarmTimeToAnnotatedString(
                    timeAdapter.getTimeFormat(alarm.time)))

            _date
                .emitChangesOnly(timeDateFormatter
                    .getAlarmDateAsString(timeAdapter.getDateFormat(alarm.time)))
            _repeatDays.emitChangesOnly(alarm.preferences.repeat.toBooleanList())

            _alarmTitle.emitChangesOnly(alarm.title)
            _snoozeMod.emitChangesOnly(alarm.preferences.snooze.toString())
            _vibrationMode.emitChangesOnly(alarm.preferences.vibration.toString())
            _ringtoneName.emitChangesOnly(alarm.preferences.ringtone.name)
        }
    }

    private fun getDefaultAlarm(): Alarm {
        return Alarm(
            alarmId = 0,
            title = "",
            time = System.currentTimeMillis(),
            enabled = true,
            preferences = AlarmPreferences(
                snooze = false,
                repeat = AlarmPreferences.RepeatPattern.Weekly(setOf(DaysOfWeeks.Friday)),
                ringtone = AlarmPreferences.AlarmRingtone(
                    "", "" // todo
                ),
                vibration = true
            )
        )
    }

    override fun loadAlarmById(alarmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.getAlarmWithId(alarmId).collectLatest { result ->
                result.handleData(
                    onSuccess = {alarm ->
                        this@AlarmDataScreenVM.alarm = alarm
                    },
                    onFailure = {/*todo*/}
                )
            }
        }
    }

    private fun getLocalTime(hours: Int, minutes: Int): LocalTime {
        return LocalTime.of(
            hours,
            minutes
        )
    }

    override val onAlarmTimePick: (hour: Int, minute: Int) -> Unit = {hour, minute ->
        viewModelScope.launch(Dispatchers.Main) {
            //alarmScheduleTime.emit(LocalDateTime.of(localDate.value, LocalTime.of(hour, minute)))
            val dateTime = LocalDateTime.of(timeAdapter.getDateFormat(alarm.time),
                getLocalTime(hour, minute))

            alarm = alarm.copy(
                time = timeAdapter
                    .getTimeInMillis(
                        dateTime
                    )
            )

            Log.d(TAG, "passed hour: ${hour}, minute: ${minute}|| dateTime: date ${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year}")
        }
    }

    override val onDayRepeatPick: (Int) -> Unit = { index ->
        viewModelScope.launch(Dispatchers.Main) {
            val isScheduled = alarm.preferences.repeat.activeDays.contains(getFromIndex(index))
            alarm = if (isScheduled)
                alarm.copy(
                    preferences = alarm.preferences.copy(
                        repeat = AlarmPreferences.RepeatPattern.Weekly(alarm.preferences.repeat.activeDays.minus(
                            getFromIndex(index)
                        ))
                    )
                )
            else
                alarm.copy(
                    preferences = alarm.preferences.copy(
                        repeat = AlarmPreferences.RepeatPattern.Weekly(alarm.preferences.repeat.activeDays.plus(
                            getFromIndex(index)
                        ))
                    )
                )
        }
    }

    override val onAlarmTitleChange: (String) -> Unit = { newTitle ->
        alarm = alarm.copy(title = newTitle)
    }

    override val onDatePickerPick: (LocalDate) -> Unit = {newDate ->
        viewModelScope.launch(Dispatchers.Default) {
            val dateTime = LocalDateTime.of(newDate,
                timeAdapter.getTimeFormat(alarm.time))

            alarm = alarm.copy(
                time = timeAdapter
                    .getTimeInMillis(
                        dateTime
                    )
            )

            Log.d(TAG, "passed hour: ${dateTime.hour}, minute: ${dateTime.minute}|| dateTime: date ${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year}")

        }
    }

    override val onSoundPickerClick: () -> Unit = {
        // Todo
    }

    override val onSoundPickResult: (Ringtone?) -> Unit = {ringTone ->
        if (ringTone != null)
            alarm = alarm.copy(preferences = alarm.preferences.copy(
                ringtone = AlarmPreferences.AlarmRingtone(
                    name = ringTone.title,
                    reference = ringTone.uri.toString()
                )
            ))
    }

    override val onVibrationPickerClick: () -> Unit = {
        alarm = alarm.copy(preferences = alarm.preferences.copy(vibration = !alarm.preferences.vibration))
    }

    override val onSnoozePickerClick: () -> Unit = {
        // Todo
    }

    override val onSaveClick: (Context) -> Unit = {

        CoroutineScope(Dispatchers.IO).launch {

            dbRepository.addOrUpdateAlarm(
                alarm
            )

            withContext(Dispatchers.Default){
                alarmScheduler.scheduleOrUpdate(alarm).handleData({
                    Log.d(TAG, "successfully scheduled alarm")
                }) { Log.d(TAG, "failed to schedule alarm") }
            }
        }

    }

    override val onCancelClick: (Context) -> Unit = {

    }
}