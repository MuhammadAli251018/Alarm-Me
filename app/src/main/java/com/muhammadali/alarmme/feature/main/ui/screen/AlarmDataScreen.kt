package com.muhammadali.alarmme.feature.main.ui.screen

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.ui.component.DaysRepeatPicker
import com.muhammadali.alarmme.common.ui.component.Picker
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.main.ui.screen.util.Date
import com.muhammadali.alarmme.feature.main.ui.screen.util.Month
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time
import com.muhammadali.alarmme.feature.main.ui.screen.util.toTextFormat


private object APreviewValues{
    val ringTime = Time(12, 49)
    val alarmTime = Time(6,0)
    val date = Date(2023, Month.July, 20)
    val repeat = arrayOf(true, true, false, true, true, false, false, )
    val alarmTitle = "Wake up"
    val ringtoneName = "bla"
    val vibrationMode = "vibrate"
    val snoozeMode = "5, 3"
}

/*
* screen structure
* 1- time picker
* 2- row of date & date picker icon (open calender dialog)
* 3- alarm name text field
* 4- sound of alarm -> pick sound
* 5- vibration -> enable / disable
* 6- snooze setting picker
* 7- row of save & cancel buttons
* */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDataScreen(
    ringTime: Time = APreviewValues.ringTime,
    alarmTime: Time = APreviewValues.alarmTime,
    date: Date = APreviewValues.date,
    repeatInitialValue: Array<Boolean> = APreviewValues.repeat,
    alarmTitle: String = APreviewValues.alarmTitle,
    ringtoneName: String = APreviewValues.ringtoneName,
    vibrationMode: String = APreviewValues.vibrationMode,
    snoozeMode: String = APreviewValues.snoozeMode,
    onAlarmTimeClick: (Time) -> Unit = {},
    onDatePickClick: () -> Unit = {},
    onSoundPickerClick: () -> Unit = {},
    onVibrationPickerClick: () -> Unit = {},
    onSnoozePickerClick: () -> Unit = {},
    onSaveCancelClick: (save: Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "ringing after: ${ringTime.toTextFormat()}",
            fontSize = 15.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(15.dp))


        Card (
            modifier= Modifier.clickable {
                onAlarmTimeClick(alarmTime)
            },
            colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(15.dp),
                text = alarmTime.toTextFormat(),
                fontSize = 80.sp,
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
                                   onDatePickClick()
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
                repeat = repeat.apply {
                    this[index] = !this[index]
                }
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                focusedLabelColor = labelColor,
                focusedBorderColor = labelColor
            ),
            onValueChange = { newValue ->
                if (newValue.length <= maxLength)
                    title = newValue
                else
                    label = "Characters limit reached"; labelColor = Color.Red
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        for (index in 0 .. 2) {

            val text: String
            val iconText: String
            val onClick: () -> Unit

            when(index) {
                0 -> {
                    text = "Sound"
                    iconText = ringtoneName
                    onClick = onSoundPickerClick
                }
                1 -> {
                    text = "Vibration"
                    iconText = vibrationMode
                    onClick = onVibrationPickerClick
                }
                else -> {
                    text = "Snooze"
                    iconText = snoozeMode
                    onClick = onSnoozePickerClick
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
                            .clickable {
                                onSaveCancelClick(i == 0)
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
    AlarmMeTheme {
        AlarmDataScreen()
    }
}