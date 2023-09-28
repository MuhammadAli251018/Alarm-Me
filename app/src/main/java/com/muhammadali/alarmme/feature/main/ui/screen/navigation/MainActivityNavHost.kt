package com.muhammadali.alarmme.feature.main.ui.screen.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.muhammadali.alarmme.feature.main.ui.screen.data.AlarmDataScreen
import com.muhammadali.alarmme.feature.main.ui.screen.main.MainScreen
import com.muhammadali.alarmme.feature.main.ui.util.DataScreenStartMode
import com.muhammadali.alarmme.feature.main.ui.screen.data.viewmodel.AlarmDataScreenVM
import com.muhammadali.alarmme.feature.main.ui.screen.main.viewmodel.MainScreenVM

@Composable
fun MainActivityNavHost(
    context: Context,
    dataScreenVM: AlarmDataScreenVM,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainActivityScreens
            .MainScreen.rout
    ) {

        composable(MainActivityScreens.MainScreen.rout) {
            //val viewModel = viewModel<MainScreenVM>()

            MainScreen(
                navController = navController
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

            //val viewModel = viewModel<AlarmDataScreenVM>()
            AlarmDataScreen(
                navController = navController,
                context = context,
                viewModel = dataScreenVM,
                screenMode = launchMode
            )
        }
    }
}