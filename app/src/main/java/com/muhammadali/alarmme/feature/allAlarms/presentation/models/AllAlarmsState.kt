package com.muhammadali.alarmme.feature.allAlarms.presentation.models

import com.muhammadali.alarmme.feature.allAlarms.presentation.components.AlarmItemState

data class AllAlarmsState(val alarms: List<AlarmItemState>) {
    companion object {
        val default = AllAlarmsState(emptyList())
    }
}