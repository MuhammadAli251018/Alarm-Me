package com.muhammadali.alarmme.feature.allAlarms.presentation

import com.muhammadali.alarmme.common.presentation.BaseViewModel
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsEffect
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsEvent
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsState

class AllAlarmsViewModel : BaseViewModel<AllAlarmsState, AllAlarmsEvent, AllAlarmsEffect> (
    initialState = AllAlarmsState.default
) {
    override fun handleEvent(event: AllAlarmsEvent) {
        when(event) {
            AllAlarmsEvent.AddAlarmEvent -> TODO()
            is AllAlarmsEvent.AlarmClickedEvent -> TODO()
            is AllAlarmsEvent.AlarmEnableOrDisableEvent -> TODO()
        }
    }
}