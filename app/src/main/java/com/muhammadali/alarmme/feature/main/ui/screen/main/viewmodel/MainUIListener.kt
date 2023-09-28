package com.muhammadali.alarmme.feature.main.ui.screen.main.viewmodel

import android.content.Context
import com.muhammadali.alarmme.feature.main.ui.screen.UIStateManager

interface MainUIListener : UIStateManager<MainUIState> {
    fun onAlarmItemClick(id: Int)

    fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean, context: Context)

    fun onAddBtnClick()
}