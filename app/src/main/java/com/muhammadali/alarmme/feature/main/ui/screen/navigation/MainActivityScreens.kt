package com.muhammadali.alarmme.feature.main.ui.screen.navigation

sealed class MainActivityScreens(val rout: String) {
    object MainScreen : MainActivityScreens("main_screen")
    object AlarmDataScreen : MainActivityScreens("alarm_data_screen")
}
