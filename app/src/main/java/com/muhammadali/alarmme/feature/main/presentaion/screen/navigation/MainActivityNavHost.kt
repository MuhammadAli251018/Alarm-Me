package com.muhammadali.alarmme.feature.main.presentaion.screen.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.AlarmDataScreen
import com.muhammadali.alarmme.feature.main.presentaion.screen.main.MainScreen
import com.muhammadali.alarmme.feature.main.presentaion.util.DataScreenStartMode
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel.AlarmDataScreenVM
import com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel.MainScreenVM

@Composable
fun MainActivityNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainActivityScreens
            .MainScreen.route
    ) {

        composable(MainActivityScreens.MainScreen.route) {
            val viewModel = hiltViewModel<MainScreenVM>()

            MainScreen(
                navController = navController,
                presenter = viewModel
            )
        }


        composable(
            route = MainActivityScreens
            .AlarmDataScreen.route + "/{launchMode}",
            arguments = listOf(
                navArgument(
                    name = "launchMode",
                ) {
                    type = NavType.IntType
                }
            )
        ) {
            val mode = it.arguments?.getInt("launchMode") ?: -1
            val launchMode =
                if (mode == -1)
                DataScreenStartMode.CreateNewAlarmMode
                else
                    DataScreenStartMode.UpdateAlarmMode(mode)

            val viewModel = hiltViewModel<AlarmDataScreenVM>()
            AlarmDataScreen(
                navController = navController,
                presenter = viewModel,
                screenMode = launchMode
            )
        }
    }
}