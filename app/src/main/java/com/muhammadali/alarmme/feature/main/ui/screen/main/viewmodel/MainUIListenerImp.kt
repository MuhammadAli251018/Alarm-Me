package com.muhammadali.alarmme.feature.main.ui.screen.main.viewmodel

import android.content.Context
import com.muhammadali.alarmme.feature.main.ui.screen.UIStateManager

class MainUIListenerImp (
    initialUIStateValue: MainUIState = MainUIState(listOf())
    ) : MainUIListener,
        UIStateManager<MainUIState> by MainUIStateManager(initialValue = initialUIStateValue)
{
    private var updateAlarmCallback: (id: Int, scheduled: Boolean) -> Unit = { _, _ -> }

    override fun onAlarmItemClick(id: Int) {
        //TODO("Not yet implemented")
     }

    override fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean, context: Context) {
        updateAlarmCallback(id, scheduled)
    }

    override fun onAddBtnClick() {
        //TODO("Not yet implemented")
    }

}