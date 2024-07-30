package com.muhammadali.alarmme.feature.main.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.ui.component.DaysRepeatPicker
import com.muhammadali.alarmme.common.ui.component.Picker
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.main.ui.component.DatePicker
import com.muhammadali.alarmme.feature.main.ui.component.TimePicker
import com.muhammadali.alarmme.feature.main.ui.util.DataScreenStartMode
import com.muhammadali.alarmme.feature.main.ui.util.PickRingtoneContract
import com.muhammadali.alarmme.feature.main.ui.util.toAnnotatedString
import com.muhammadali.alarmme.feature.main.ui.util.toTextFormat
import com.muhammadali.alarmme.feature.main.ui.viewmodels.main.AlarmDataScreenVM
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun AlarmDataScreen(
    context: Context,
    viewModel: AlarmDataScreenVM,
    screenMode: DataScreenStartMode
) {
    if (screenMode == DataScreenStartMode.CreateNewAlarmMode) {
        //todo make the view model load data
        val ringTime by remember {viewModel.ringingTime}
        val alarmTitle by remember {viewModel.alarmTitle}
        val ringtoneName by remember {viewModel.ringtoneName}
        val vibrationMode by remember {viewModel.vibrationMode}

        AlarmDataScreen(
            context = context,
            ringTime = ringTime,
            alarmTimeInit = viewModel.alarmTime.value,
            dateInit = viewModel.date.value,
            repeatInitialValue = viewModel.repeatingPattern.value,
            alarmTitle = alarmTitle,
            ringtoneName = ringtoneName,
            vibrationMode = vibrationMode,
            snoozeModeInit = viewModel.snoozeMode.value,
            onAlarmTimePick = viewModel::onAlarmTimePick,
            onDatePickerPick = viewModel::onAlarmDatePick,
            onSoundPickResult = viewModel::onSoundPickResult,
            onVibrationPickerClick = viewModel::onVibrationPickerClick,
            onSoundPickerClick = viewModel::onSoundPickerClick,
            onSnoozePickerClick = viewModel::onSnoozePickerClick,
            onSaveCancelClick = viewModel::onSaveBtnClick
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDataScreen(
    context: Context,
    ringTime: LocalTime,
    alarmTimeInit: LocalTime,
    dateInit: LocalDate,
    repeatInitialValue: Array<Boolean>,
    alarmTitle: String,
    ringtoneName: String,
    vibrationMode: Boolean,
    snoozeModeInit: Boolean,
    onAlarmTimePick: (LocalTime) -> Unit,
    onDatePickerPick: (LocalDate) -> Unit,
    onSoundPickerClick: () -> Unit,
    onSoundPickResult: (Uri) -> Unit,
    onVibrationPickerClick: (Boolean) -> Unit,
    onSnoozePickerClick: (Boolean) -> Unit,
    onSaveCancelClick: (save: Boolean, Context) -> Unit
) {

    val timePickerState = rememberUseCaseState(false)
    val datePickerState = rememberUseCaseState(false)
    //var isSnoozePVisible by remember { mutableStateOf(false) }
    var snoozeData by remember { mutableStateOf(snoozeModeInit) }
    var alarmTime by remember { mutableStateOf(alarmTimeInit) }
    var date by remember { mutableStateOf(dateInit) }

    val launcher = rememberLauncherForActivityResult(
        contract = PickRingtoneContract()) {
        if (it != null)
            onSoundPickResult(it)

        //todo handle
    }

    //dialogs
    TimePicker(
        state = timePickerState,
        onPositiveClick = { hours, minutes ->
            alarmTime = LocalTime.of(hours,minutes)
            onAlarmTimePick(alarmTime)
            timePickerState.hide()
        },
        onNegativeClick = {
            timePickerState.hide()
        }
    )

    DatePicker(
        state = datePickerState,
        onSelectDate = {selectDate ->
            date = selectDate
            datePickerState.hide()
            onDatePickerPick(selectDate)
        },
        onNegativeClick = {
            datePickerState.hide()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "ringing after: ${ringTime.hour} hour and ${ringTime.minute} minute",
            fontSize = 15.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(15.dp))

        Card (
            colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
            ),
            onClick = {
                timePickerState.show()
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(15.dp),
                text = alarmTime.toAnnotatedString(
                    timeStyle = SpanStyle(fontSize = 80.sp),
                    periodStyle = SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                ),
                //fontSize = 60.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                text = date.toTextFormat(),
                fontSize = 20.sp,
                color= Color.White
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            datePickerState.show()
                        },
                    painter = painterResource(R.drawable.ic_calendar_24),
                    contentDescription = "Pick alarm date",
                    tint = Color.White
                )
            }
        }
        var repeat by remember { mutableStateOf(repeatInitialValue) }

        DaysRepeatPicker(
            repeat = repeat,
            fontSize = 30.sp,
            spacesCount = 20,
            onDayClick = {index ->
                val newRepeat = mutableListOf<Boolean>()
                repeat.forEachIndexed { i: Int, value: Boolean ->
                    newRepeat.add(if (i == index) !value else value)
                }
                repeat = newRepeat.toTypedArray()
            }
        )

        Spacer(modifier = Modifier.height(15.dp))

        var title by remember { mutableStateOf(alarmTitle) }
        var label by remember { mutableStateOf("Alarm Title") }
        var labelColor by remember { mutableStateOf(Color.White) }
        val maxLength = 10


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            label = {
                    Text(text = label)
            },
            value = title,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = labelColor,
                focusedLabelColor = labelColor,
            ),
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    title = newValue
                    label = "Alarm Title"
                    labelColor = Color.White
                }
                else {
                    label = "Characters limit reached"
                    labelColor = Color.Red
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        for (index in 0 .. /*2*/ 1) {

            val text: String
            val iconText: String
            val onClick: () -> Unit

            when(index) {
                0 -> {
                    text = "Sound"
                    iconText = ringtoneName
                    onClick =  {
                        onSoundPickerClick()
                        launcher.launch(null)
                    }
                }
                1 -> {
                    text = "Vibration"
                    iconText = vibrationMode.toString()
                    onClick = { onVibrationPickerClick(vibrationMode) }
                }
                else -> {
                    text = "Snooze"
                    iconText = if(snoozeData) "On" else "Off"
                    onClick = {
                        onSnoozePickerClick(snoozeData)
                        snoozeData =! snoozeData
                    }
                }
            }

            Card(
                modifier = Modifier
                    .padding(20.dp)
                    ,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {

                Picker(
                    modifier = Modifier.padding(20.dp),
                    text = text,
                    textColor = Color.White,
                    fontSize = 20.sp,
                    iconText = iconText,
                    iconTextColor = MaterialTheme.colorScheme.primary,
                    iconTextFontSize = 20.sp,
                    iconPainter = painterResource(id = R.drawable.ic_left_arrow_24),
                    iconTint = MaterialTheme.colorScheme.primary,
                    iconSize = 20.dp,
                    iconDescription = "Change sound",
                    onPickerClick = onClick
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {

                for (i in 0 .. 1) {

                    val isSaveBtn = i == 0

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 20.dp)
                            .clickable {
                                onSaveCancelClick(i == 0, context)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSaveBtn) "Save" else "Cancel",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmDataScreenPreview() {

    /*val ringTime = LocalTime.of(12, 49)
    val alarmTime = LocalTime.of(6,0)
    val date = LocalDate.of(2023, 7, 20)
    val repeat = arrayOf(true, true, false, true, true, false, false)
    val alarmTitle = "Wake up"
    val ringtoneName = "bla"
    val vibrationMode = true


    AlarmMeTheme {
        AlarmDataScreen(
            ringTime = ringTime,
            alarmTimeInit = alarmTime,
            dateInit = date,
            repeatInitialValue = repeat,
            alarmTitle = alarmTitle,
            ringtoneName = ringtoneName,
            vibrationMode = vibrationMode,
            snoozeModeInit = true,
            onAlarmTimePick = {},
            onDatePickerPick = {},
            onSoundPickerClick = {},
            onVibrationPickerClick = {},
            onSnoozePickerClick = {},
            onSaveCancelClick = {},
            onSoundPickResult = {}

        )
    }*/
}