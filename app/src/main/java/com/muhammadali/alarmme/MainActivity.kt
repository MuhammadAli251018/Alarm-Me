package com.muhammadali.alarmme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.muhammadali.alarmme.feature.alarmList.presentation.AlarmListScreen
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmMeThemeProvider {
                Navigator(AlarmListScreen())
            }
        }
    }
}
