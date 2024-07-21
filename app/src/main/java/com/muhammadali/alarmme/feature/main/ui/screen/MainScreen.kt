package com.muhammadali.alarmme.feature.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.main.ui.component.AlarmItem
import com.muhammadali.alarmme.feature.main.ui.component.util.AlarmItemState
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time
import com.muhammadali.alarmme.feature.main.ui.screen.util.toAnnotatedString

/*
* Main Screen structure
* 1- text have value -> "Alarms"
* 2- vertical grid of the alarms info
* 3- button to add new alarm
* */

@Composable
fun MainScreen(
    alarms: List<AlarmItemState>,
    onItemClick: (index: Int) -> Unit,
    onItemSwitchClick: (index: Int, isScheduled: Boolean) -> Unit,
    onAddBtnClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Alarms",
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                itemsIndexed(items= alarms) {index,itemState ->

                    AlarmItem(
                        title = itemState.alarmTitle,
                        time = itemState.alarmTime,
                        repeat = itemState.alarmRepeat,
                        isScheduledInitValue = itemState.isScheduled,
                        isEnabled = itemState.isEnabled,
                        onItemClick = { onItemClick(index) },
                        onSwitchClick = {onItemSwitchClick(index, it)}
                    )
                }
            }
        }

        IconButton(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .size(70.dp)
                .align(Alignment.BottomCenter),
            onClick = onAddBtnClick
        ) {

            Card (
                modifier= Modifier
                    .fillMaxSize(),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        modifier= Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_add_24),
                        contentDescription = "Add alarm",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    val title = "title"
    val time = Time(6, 30).toAnnotatedString(SpanStyle(fontSize = 32.sp), SpanStyle(fontSize = 16.sp))
    val repeat = arrayOf(false, true,  true,  false,  false,  true,  true)
    val isScheduled = true
    val isEnabled = true
    val state = AlarmItemState(title, time, repeat, isScheduled, isEnabled)
    val alarms = listOf(state, state, state, state)

    AlarmMeTheme(
        dynamicColor = false
    ) {
        MainScreen(alarms = alarms, onItemClick = {}, onItemSwitchClick = {_,_ ->}) {}
    }
}