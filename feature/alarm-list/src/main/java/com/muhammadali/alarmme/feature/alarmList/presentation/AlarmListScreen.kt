package com.muhammadali.alarmme.feature.alarmList.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.datetime.LocalDateTime

sealed interface NextAlarmsFireTime {
    data object NoAlarms : NextAlarmsFireTime
    data class NextAlarm(val time: LocalDateTime) : NextAlarmsFireTime
}

class AlarmListScreen : Screen {

    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }
}