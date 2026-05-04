package com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import com.muhammadali.alarmme.feature.main.presentaion.util.Ringtone
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface DataScreenPresenter {

    // States
    val ringingTime: StateFlow<AnnotatedString>
    val alarmTime: StateFlow<AnnotatedString>
    val date: StateFlow<String>
    val repeatDays: StateFlow<List<Boolean>>
    val alarmTitle: StateFlow<String>
    val snoozeMode: StateFlow<String>
    val ringtoneName: StateFlow<String>
    val vibrationMode: StateFlow<String>


    //  Events
    val onAlarmTimePick: (hour: Int, minute: Int) -> Unit
    val onDayRepeatPick: (Int) -> Unit
    val onAlarmTitleChange: (String) -> Unit
    val onDatePickerPick: (LocalDate) -> Unit
    val onSoundPickerClick: () -> Unit
    val onSoundPickResult: (Ringtone?) -> Unit
    val onVibrationPickerClick: () -> Unit
    val onSnoozePickerClick: () -> Unit
    val onSaveClick: (Context) -> Unit
    val onCancelClick: (Context) -> Unit

    fun loadAlarmById(alarmId: Int)
}