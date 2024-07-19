package com.muhammadali.alarmme.feature.main.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.common.ui.component.DaysRepeatPicker
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme

//todo fix repeat & actions & make overload function with bundle
private object PreviewValues{
    val title = "title"
    val time = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 32.sp)) {
            append("11:35 ")
        }

        withStyle(style = SpanStyle(fontSize = 16.sp)) {
            append("AM")
        }
    }

    val repeat = arrayOf(false, true,  true,  true,  true,  true,  true).toBooleanArray()
    val isScheduled = true
    val isEnabled = true

    val state = AlarmItemState(title, time, repeat, isScheduled, isEnabled)
    val action = AlarmItemAction({}, {})
}

@Composable
fun AlarmItem(
    state: AlarmItemState = PreviewValues.state,
    action: AlarmItemAction = PreviewValues.action
    ) {
    var isScheduled by remember { mutableStateOf(state.isScheduled) }

    Card (
        modifier= Modifier
            .padding(5.dp)
            //todo fix size and make it responsive
            .size(170.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .clickable{action.onItemClick()},
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier= Modifier
                    .align(Alignment.Start)
                    .weight(.75f)
                ,
                text = state.alarmTitle,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                color = Color.White
            )


            Text(
                modifier = Modifier.weight(1.25f),
                text = state.alarmTime,
                color = Color.White,
            )

            DaysRepeatPicker()

            Switch(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.End),
                checked = isScheduled,
                onCheckedChange = {isEnabled ->
                    isScheduled = isEnabled
                    action.onSwitchBtnClick(isEnabled)
                },
                colors= SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    disabledCheckedThumbColor = Color.White,
                    disabledUncheckedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    disabledCheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
                    disabledUncheckedTrackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = .3f),
                ),
                /* todo is enabled should be the whole element & the change should be
                    only in the background color
                    */
                enabled = state.isEnabled
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmItemPreview() {
    AlarmMeTheme(
        dynamicColor = false

    ) {
        AlarmItem()
    }
}
