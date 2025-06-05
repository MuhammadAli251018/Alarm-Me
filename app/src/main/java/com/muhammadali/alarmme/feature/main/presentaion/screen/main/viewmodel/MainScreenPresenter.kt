package com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel

import com.muhammadali.alarmme.feature.allAlarms.presentation.components.AlarmItemState
import kotlinx.coroutines.flow.StateFlow

interface MainScreenPresenter {

    val alarms: StateFlow<List<AlarmItemState>>

    fun onAlarmItemClick(id: Int)

    fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean)

    fun onAddBtnClick()
}