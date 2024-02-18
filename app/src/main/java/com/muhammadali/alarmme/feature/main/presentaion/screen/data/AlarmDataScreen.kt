package com.muhammadali.alarmme.feature.main.presentaion.screen.data

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.ui.component.DaysRepeatPicker
import com.muhammadali.alarmme.common.ui.component.Picker
import com.muhammadali.alarmme.feature.main.presentaion.component.DatePicker
import com.muhammadali.alarmme.feature.main.presentaion.component.TimePicker
import com.muhammadali.alarmme.feature.main.presentaion.screen.navigation.MainActivityScreens
import com.muhammadali.alarmme.feature.main.presentaion.util.DataScreenStartMode
import com.muhammadali.alarmme.feature.main.presentaion.util.PickRingtoneContract
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel.DataScreenPresenter
import com.muhammadali.alarmme.feature.main.presentaion.util.Ringtone
import com.muhammadali.alarmme.feature.main.presentaion.util.toAnnotatedString
import com.muhammadali.alarmme.feature.main.presentaion.util.toTextFormat
import java.time.LocalDate
import java.time.LocalTime


object AlarmDataScreenPreview {
    val ringTime = buildAnnotatedString {
        val time = LocalTime.now()

        append("ringing after ")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(time.hour.toString())
        }

        append("and ")

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(time.minute.toString())
        }
    }
    val alarmTime = LocalTime.now().toAnnotatedString()
    val date = LocalDate.now().toTextFormat()
    val repeat = listOf(true, true, false, true, true, false, false)
    val alarmTitle = "Wake up"
    val ringtoneName = "bla"
    val vibrationMode = "Off"
    val snoozeMode = "Off"
    val onAlarmTimePick: (Int, Int) -> Unit = {_, _ -> }
    val onDayRepeatPick: (Int) -> Unit = {}
    val onAlarmTitleChange: (String) -> Unit = {}
    val onDatePickerPick: (LocalDate) -> Unit = {}
    val onSoundPickerClick: () -> Unit = {}
    val onSoundPickResult: (Ringtone?) -> Unit = {}
    val onVibrationPickerClick: () -> Unit = {}
    val onSnoozePickerClick: () -> Unit = {}
    val onSaveClick: (Context) -> Unit = {}
    val onCancelClick: (Context) -> Unit = {}
}

@Composable
fun AlarmDataScreen(
    navController: NavHostController,
    screenMode: DataScreenStartMode,
    presenter: DataScreenPresenter
) {
    if (screenMode != DataScreenStartMode.CreateNewAlarmMode) {
        presenter.loadAlarmById(screenMode.data)
    }
    val datePickerState = rememberUseCaseState(false)
    val timePickerState = rememberUseCaseState(false)
    val ringingTime by presenter.ringingTime.collectAsStateWithLifecycle()
    val date by presenter.date.collectAsStateWithLifecycle()
    val repeatDays by presenter.repeatDays.collectAsStateWithLifecycle()
    val alarmTitle by presenter.alarmTitle.collectAsStateWithLifecycle()
    val snoozeMode by presenter.snoozeMode.collectAsStateWithLifecycle()
    val ringtoneName by presenter.ringtoneName.collectAsStateWithLifecycle()
    val alarmTime by presenter.alarmTime.collectAsStateWithLifecycle()
    val vibrationMode by presenter.vibrationMode.collectAsStateWithLifecycle()

    AlarmDataContent(
        ringTime = ringingTime,
        alarmTime = alarmTime,
        date = date,
        timePickerState = timePickerState,
        datePickerState = datePickerState,
        repeatDays = repeatDays,
        alarmTitle = alarmTitle,
        snoozeMode = snoozeMode,
        ringtoneName = ringtoneName,
        vibrationMode = vibrationMode,
        onAlarmTimePick = presenter.onAlarmTimePick,
        onDayRepeatPick = presenter.onDayRepeatPick,
        onAlarmTitleChange = presenter.onAlarmTitleChange,
        onDatePickerPick = presenter.onDatePickerPick,
        onSoundPickerClick = presenter.onSoundPickerClick,
        onSoundPickResult = presenter.onSoundPickResult,
        onVibrationPickerClick = presenter.onVibrationPickerClick,
        onSaveClick = presenter.onSaveClick,
        onCancelClick = presenter.onCancelClick,
        onSnoozePickerClick = presenter.onSnoozePickerClick,
        navigateToMainScreen = {
            navController.navigate(MainActivityScreens.MainScreen.rout)
        }
    )
}


// stateless

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDataContent(
    ringTime: AnnotatedString,
    alarmTime: AnnotatedString,
    date: String,
    timePickerState: UseCaseState,
    datePickerState: UseCaseState,
    repeatDays: List<Boolean>,
    alarmTextFieldLabelContent: String = "",
    alarmTextFieldLabelColor: Color = Color.Red,
    alarmTitle: String,
    snoozeMode: String,
    ringtoneName: String,
    vibrationMode: String,
    navigateToMainScreen: () -> Unit,
    onAlarmTimePick: (Int, Int) -> Unit,
    onDayRepeatPick: (Int) -> Unit,
    onAlarmTitleChange: (String) -> Unit,
    onDatePickerPick: (LocalDate) -> Unit,
    onSoundPickerClick: () -> Unit,
    onSoundPickResult: (Ringtone?) -> Unit,
    onVibrationPickerClick: () -> Unit,
    onSnoozePickerClick: () -> Unit,
    onSaveClick: (Context) -> Unit,
    onCancelClick: (Context) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = PickRingtoneContract(), onSoundPickResult)

    //dialogs
    TimePicker(
        state = timePickerState,
        onPositiveClick = onAlarmTimePick,
        onNegativeClick = {
            timePickerState.hide()
        }
    )

    DatePicker(
        state = datePickerState,
        onSelectDate = onDatePickerPick,
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
            text = ringTime,
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
                text = alarmTime,
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
                text = date,
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

        DaysRepeatPicker(
            repeat = repeatDays,
            fontSize = 30.sp,
            spacesCount = 20,
            onDayClick = onDayRepeatPick
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            label = {
                    Text(text = alarmTextFieldLabelContent)
            },
            value = alarmTitle,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = alarmTextFieldLabelColor,
                focusedLabelColor = alarmTextFieldLabelColor,
            ),
            onValueChange = onAlarmTitleChange
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
                    iconText = vibrationMode
                    onClick = { onVibrationPickerClick() }
                }
                else -> {
                    text = "Snooze"
                    iconText = snoozeMode
                    onClick = onSnoozePickerClick
                }
            }

            Card(
                modifier = Modifier
                    .padding(20.dp),
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
                    val context = LocalContext.current

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 20.dp)
                            .clickable {
                                if (isSaveBtn)
                                    onSaveClick(context)
                                else
                                    onCancelClick(context)

                                navigateToMainScreen()
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

    /*AlarmDataScreen(
        navController = rememberNavController(),
        screenMode = DataScreenStartMode.CreateNewAlarmMode
    )*/
}