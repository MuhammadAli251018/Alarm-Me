package com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel

import android.content.Context
import com.muhammadali.alarmme.feature.main.presentaion.component.util.AlarmItemState
import kotlinx.coroutines.flow.Flow

interface MainScreenPresenter {

    val alarms: Flow<List<AlarmItemState>>

    fun onAlarmItemClick(id: Int)

    fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean, context: Context)

    fun onAddBtnClick()
}