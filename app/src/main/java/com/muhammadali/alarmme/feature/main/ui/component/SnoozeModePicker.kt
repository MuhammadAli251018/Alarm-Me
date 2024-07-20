package com.muhammadali.alarmme.feature.main.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.main.ui.component.util.SnoozeMode
import com.muhammadali.alarmme.feature.main.ui.component.util.SnoozeRepeat
import com.muhammadali.alarmme.feature.main.ui.component.util.getAllSnoozeRepeat

@Composable
fun SnoozeModePicker(
    showDialog: Boolean = true,
    repeat: Array<SnoozeRepeat> = getAllSnoozeRepeat(),
    chosenModeInit: SnoozeMode = SnoozeMode(SnoozeRepeat.FiveTimes, -1),
    periods: Array<Int> = arrayOf(3, 5, -1),
    onChooseMode: (SnoozeMode?) -> Unit ={},
    onClosePicker: () -> Unit = {}
) {
    if(showDialog) {

        var chosenMode by remember { mutableStateOf(chosenModeInit) }
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

                    ChooseColumn(
                        data = periods.map {"$it minutes"}.toTypedArray(),
                        chosenIndex = periods.indexOf(chosenMode.period)
                    ) {
                        chosenMode = SnoozeMode(chosenMode.repeat, it)
                    }

                    Divider(Modifier.fillMaxWidth(), thickness = 1.dp)

                    Spacer(modifier = Modifier.height(20.dp))

                    ChooseColumn(
                        data = repeat.map { it.content }.toTypedArray(),
                        chosenIndex = repeat.indexOf(chosenMode.repeat)
                    ) {
                        chosenMode = SnoozeMode(chosenMode.repeat, it)
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

@Preview(showBackground = true)
@Composable
fun PreviewSnoozeModePicker() {
    AlarmMeTheme {
        SnoozeModePicker()
    }
}