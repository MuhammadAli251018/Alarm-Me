package com.muhammadali.alarmme.feature.main.ui.screen.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.muhammadali.alarmme.feature.main.ui.screen.AlarmDataScreen
import com.muhammadali.alarmme.feature.main.ui.screen.MainScreen
import com.muhammadali.alarmme.feature.main.ui.util.DataScreenStartMode
import com.muhammadali.alarmme.feature.main.ui.viewmodels.main.AlarmDataScreenVM
import com.muhammadali.alarmme.feature.main.ui.viewmodels.main.MainScreenVM

@Composable
fun MainScreenNavHost(
    context: Context,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainActivityScreens
            .MainScreen.rout) {

        composable(MainActivityScreens.MainScreen.rout) {
            val viewModel = viewModel<MainScreenVM>()

            MainScreen(
                alarms = viewModel.alarmsStateList.value,
                onItemClick = viewModel::onItemClick,
                onItemSwitchClick = {   index, scheduled ->
                    viewModel.onItemSwitchBtnClick(index, scheduled, context)
                },
                onAddBtnClick = viewModel::onAddBtnClick
            )
        }


        composable(
            route = MainActivityScreens
            .AlarmDataScreen.rout + "/{launch_mode}",
            arguments = listOf(
                navArgument(
                    name = "launch_mode",
                ) {
                    type = NavType.IntType
                }
            )
        ) {
            val mode = it.arguments?.getInt("launch_mode") ?: -1
            val launchMode =
                if (mode == -1)
                DataScreenStartMode.CreateNewAlarmMode
                else
                    DataScreenStartMode.UpdateAlarmMode(mode)

            val viewModel = viewModel<AlarmDataScreenVM>()
            AlarmDataScreen(
                context = context,
                viewModel = viewModel,
                screenMode = launchMode
            )
        }
    }
}