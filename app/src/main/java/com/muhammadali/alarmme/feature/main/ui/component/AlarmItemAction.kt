package com.muhammadali.alarmme.feature.main.ui.component

data class AlarmItemAction(
    val onItemClick: () -> Unit,
    val onSwitchBtnClick: (Boolean) -> Unit
)
