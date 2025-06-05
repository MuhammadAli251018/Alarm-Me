package com.muhammadali.alarmme.feature.allAlarms.presentation

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.presentation.rememberDispatch
import com.muhammadali.alarmme.common.presentation.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.allAlarms.presentation.components.AlarmItem
import com.muhammadali.alarmme.feature.allAlarms.presentation.components.AlarmItemState
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsEffect
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsEvent
import com.muhammadali.alarmme.feature.main.presentaion.screen.navigation.MainActivityScreens
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllAlarmsScreen(
    navController: NavHostController
) {

    val viewModel = koinViewModel<AllAlarmsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dispatch = viewModel.rememberDispatch()
    val effects = viewModel.effect

    effects.HandleEffects(navController)

    AllAlarmsScreenContent(
        // Todo: Change the navigation to not using routes
        alarms = state.alarms,
        onItemClick = { dispatch(AllAlarmsEvent.AlarmClickedEvent(index = it)) },
        onItemSwitchClick = { dispatch(AllAlarmsEvent.AlarmEnableOrDisableEvent(index = it)) },
        onAddBtnClick = { dispatch(AllAlarmsEvent.AddAlarmEvent) }
    )
}

@Composable
fun AllAlarmsScreenContent(
    alarms: List<AlarmItemState>,
    onItemClick: (index: Int) -> Unit,
    onItemSwitchClick: (index: Int) -> Unit,
    onAddBtnClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
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

            if (alarms.isNotEmpty()){
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(items = alarms) { alarm ->
                        AlarmItem(
                            modifier = Modifier,
                            alarmState = alarm,
                            onItemClick = { onItemClick(alarm.id) },
                            onSwitchClick = { onItemSwitchClick(alarm.id) },
                        )
                    }
                }
            }
            else
                Box(modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Alarms",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
        }

        IconButton(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .size(70.dp)
                .align(Alignment.BottomCenter),
            onClick = { onAddBtnClick() }
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

@Preview
@Composable
fun AllAlarmsScreenContentPreview() {
    AlarmMeTheme {
        AllAlarmsScreenContent(
            alarms = listOf(AlarmItemState.default),
            onItemClick = {  },
            onItemSwitchClick = {  },
            onAddBtnClick = {  }
        )
    }
}

@Composable
fun SharedFlow<AllAlarmsEffect>.HandleEffects(navController: NavHostController) {
    LaunchedEffect(Unit) {
        collect {
            when(it) {
                is AllAlarmsEffect.NavigateToEditAlarmEffect -> navController.navigate(
                    route = MainActivityScreens.AlarmDataScreen.rout + "/${it.index}"
                )
                AllAlarmsEffect.NavigateToNewAlarmEffect -> navController.navigate(
                    route = MainActivityScreens.AlarmDataScreen.rout + "/-1"
                )
            }
        }
    }
}