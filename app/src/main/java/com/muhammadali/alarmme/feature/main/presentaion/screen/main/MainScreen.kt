package com.muhammadali.alarmme.feature.main.presentaion.screen.main

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.feature.main.presentaion.component.AlarmItem
import com.muhammadali.alarmme.feature.main.presentaion.component.util.AlarmItemState
import com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel.MainScreenPresenter
import com.muhammadali.alarmme.feature.main.presentaion.screen.navigation.MainActivityScreens

@Composable
fun MainScreen(
    presenter: MainScreenPresenter,
    navController: NavHostController
    ) {
    val alarms by presenter.alarms.collectAsStateWithLifecycle(emptyList())
    val context = LocalContext.current

    MainScreen(
        navigate = {alarmId ->
            navController.navigate(
                route = MainActivityScreens.AlarmDataScreen.rout + "/$alarmId"
            )
        },
        alarms = alarms,
        onItemClick = presenter::onAlarmItemClick,
        onItemSwitchClick = {   index, scheduled ->
            presenter.onSwitchBtnAlarmItemClick(index, scheduled)
        },
        onAddBtnClick = presenter::onAddBtnClick
    )
}

@Composable
fun MainScreen(
    navigate: (Int) -> Unit,
    alarms: List<AlarmItemState>,
    onItemClick: (index: Int) -> Unit,
    onItemSwitchClick: (index: Int, isScheduled: Boolean) -> Unit,
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(items = alarms) { itemState ->

                        AlarmItem(
                            title = itemState.alarmTitle,
                            time = itemState.alarmTime,
                            repeat = itemState.alarmRepeat,
                            isScheduledInitValue = itemState.isScheduled,
                            isEnabled = itemState.isScheduled,
                            onItemClick = {
                                onItemClick(itemState.alarmDBId)
                                navigate(itemState.alarmDBId)
                            },
                            onSwitchClick = { onItemSwitchClick(itemState.alarmDBId, it) }
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
            onClick = {
                onAddBtnClick()
                navigate(-1)
            }
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
    AlarmMeTheme(
        dynamicColor = false
    ) {
        MainScreen(
            navigate = {},
            alarms = emptyList(),
            onItemClick = {},
            onItemSwitchClick = {_,_ -> },
            onAddBtnClick = {}
        )
    }
}