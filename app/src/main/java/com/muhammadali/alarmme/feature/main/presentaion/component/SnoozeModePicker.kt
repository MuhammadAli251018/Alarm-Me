package com.muhammadali.alarmme.feature.main.presentaion.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.muhammadali.alarmme.common.presentation.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.main.presentaion.component.util.SnoozeRepeat
import com.muhammadali.alarmme.feature.main.presentaion.component.util.SnoozeState
import com.muhammadali.alarmme.feature.main.presentaion.component.util.getAllSnoozeRepeat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnoozeModePicker(
    showDialog: Boolean,
    repeat: Array<SnoozeRepeat> = getAllSnoozeRepeat(),
    chosenStateInit: SnoozeState,
    periods: Array<Int>,
    onChooseMode: (SnoozeState) -> Unit,
    onClosePicker: () -> Unit
) {
    if(showDialog) {
        var chosenState by remember { mutableStateOf(chosenStateInit) }

        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //val snoozeOn by remember { mutableStateOf(chosenModeInit) }

                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            chosenState = if (chosenState == SnoozeState.SnoozeOff)
                                SnoozeState.SnoozeMode(SnoozeRepeat.FiveTimes, 3)
                             else
                                 SnoozeState.SnoozeOff
                        }
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                            Text(
                                text = if(chosenState != SnoozeState.SnoozeOff) "Snooze is On" else "Snooze is Off",
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (chosenState != SnoozeState.SnoozeOff) {

                        var chosenMode: SnoozeState.SnoozeMode by remember { mutableStateOf(chosenStateInit as SnoozeState.SnoozeMode) }

                        Text(
                            text = "Snooze Repeat",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ChooseColumn(
                            data = periods.map { "$it minutes" }.toTypedArray(),
                            chosenIndex = periods.indexOf(chosenMode.period)
                        ) {
                            chosenMode = SnoozeState.SnoozeMode(chosenMode.repeat, it)
                            onChooseMode(chosenMode)
                        }

                        Divider(Modifier.fillMaxWidth(), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Snooze Time",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ChooseColumn(
                            data = repeat.map { it.content }.toTypedArray(),
                            chosenIndex = repeat.indexOf(chosenMode.repeat)
                        ) {
                            chosenMode = SnoozeState.SnoozeMode(chosenMode.repeat, it)
                            onChooseMode(chosenMode)
                        }


                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = onClosePicker) {
                            Text(text = "Save", fontSize = 20.sp, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSnoozeModePicker() {

    val showDialog = true
    val chosenModeInit = SnoozeState.SnoozeMode(SnoozeRepeat.FiveTimes, -1)
    val periods: Array<Int> = arrayOf(3, 5, -1)
    val onChooseMode: (SnoozeState) -> Unit ={}
    val onClosePicker: () -> Unit = {}

    AlarmMeTheme {
        SnoozeModePicker(
            showDialog = showDialog,
            chosenStateInit = chosenModeInit,
            periods = periods,
            onChooseMode = onChooseMode,
            onClosePicker = onClosePicker
        )
    }
}