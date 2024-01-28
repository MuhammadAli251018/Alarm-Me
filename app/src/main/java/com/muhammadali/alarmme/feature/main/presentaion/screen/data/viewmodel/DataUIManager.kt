package com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.muhammadali.alarmme.feature.main.data.local.AlarmEntity
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.presentaion.screen.UIStateManager
import com.muhammadali.alarmme.feature.main.presentaion.util.Ringtone
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

const val ALARM_MAX_TITLE_LENGTH = 15
interface DataUIManager: UIStateManager<DataUIState> {
    /*fun alarmToDataUIState(alarmEntity: AlarmEntity): DataUIState {
        val localTime = timeAdapter.getTimeFormat(alarmEntity.time)
        val localDate = timeAdapter.getDateFormat(alarmEntity.time)

        return DataUIState(
            alarmTitle = alarmEntity.title,
            alarmTime = timeDateFormatter
                .formatAlarmTimeToAnnotatedString(localTime),
            ringingTime = timeDateFormatter.formatRingingTimeToAnnotatedString(getRingingTime(localTime)),
            date = timeDateFormatter.getAlarmDateAsString(localDate),
            repeatPattern = AlarmRepeatingPattern.ofString(alarmEntity.repeat),
            vibrationMode = if (alarmEntity.vibration) "ON" else "OFF",
            snoozeMode = if (alarmEntity.snooze.toBooleanStrict()) "ON" else "OFF",
            ringToneName = "Default",
            alarmTextFieldLabelContent = getAlarmLabelContent(alarmEntity.title),
            alarmTextFieldLabelColor = getAlarmLabelColor(alarmEntity.title)
        )
    }*/


    val timeDateFormatter: TimeDateFormatter
    val timeAdapter: TimeAdapter

    fun saveAlarmData(context: Context)

    fun getRingingTime(alarmTime: LocalTime): LocalTime

    fun getLocalTime(hours: Int, minutes: Int): LocalTime

    fun getLocalDate(year: Int, month: Month ,dayOfMonth: Int): LocalTime

    fun saveNewRingtoneUri(uri: Uri)

    fun changeVibrationMode()

    fun changeSnoozeMode()

    fun onAlarmTimePick(hours: Int, minutes: Int) {
        val time = getLocalTime(hours, minutes)

        updateUIState(
            uiState.value.copy(
                alarmTime = timeDateFormatter.formatAlarmTimeToAnnotatedString(time),
                ringingTime = timeDateFormatter.formatRingingTimeToAnnotatedString(getRingingTime(time))
            )
        )
    }

    fun onDayRepeatPicker(day: Int) {
        updateUIState(uiState.value.copy(
            repeatPattern = uiState.value.repeatPattern.set(day = day, uiState.value.repeatPattern[day])
        ))
    }

    fun onAlarmTitleChange(newTitle: String) {
        updateUIState(
            uiState.value.copy(
                alarmTitle = newTitle,
                alarmTextFieldLabelColor = getAlarmLabelColor(newTitle),
                alarmTextFieldLabelContent = getAlarmLabelContent(newTitle)
            )
        )
    }

    fun onDatePickerPick(newData: LocalDate) {

        updateUIState(
            uiState.value.copy(
                date = timeDateFormatter.getAlarmDateAsString(newData)
            )
        )

        //onDateDialogClick(false)
    }

    fun onSoundPickerResult(ringtone: Ringtone?) {
        if (ringtone != null) {
            updateUIState(
                uiState.value.copy(
                    ringToneName = ringtone.title
                )
            )
            saveNewRingtoneUri(ringtone.uri)
        }
        else {
            TODO("Show err include no new ringtone is chosed")
        }

        //onTimeDialogClick(false)
    }

    fun onSoundPickerClick() {
        //onTimeDialogClick(true)
    }

    fun onVibrationPickerClick() {
        changeVibrationMode()
    }


    fun onSnoozePickerClick() {
        changeVibrationMode()
    }

    fun onSaveCancelClick(save: Boolean, context: Context) {
        if (save)
            saveAlarmData(context)
    }

    fun getAlarmLabelContent(alarmTitle: String): String {
        return if (alarmTitle.length > ALARM_MAX_TITLE_LENGTH)
            "Too long title"
        else if (alarmTitle.isEmpty())
            "Alarm Title"
        else
            "Remaining ${alarmTitle.length - ALARM_MAX_TITLE_LENGTH}"
    }

    fun getAlarmLabelColor(alarmTitle: String): Color {
        return if (alarmTitle.length > ALARM_MAX_TITLE_LENGTH)
            Color.White
        else
            Color.Red
    }


}