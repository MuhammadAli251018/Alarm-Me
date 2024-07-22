package com.muhammadali.alarmme.feature.main.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muhammadali.alarmme.feature.main.ui.screen.MainScreen

@Composable
fun MainScreenNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainActivityScreens
            .MainScreen.rout) {

        composable(MainActivityScreens.MainScreen.rout) {
            //todo pass the the view models
        }


        composable(MainActivityScreens.AlarmDataScreen.rout) {
            //todo pass the the view models
        }
    }
}