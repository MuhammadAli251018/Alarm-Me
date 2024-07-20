package com.muhammadali.alarmme.feature.main.ui.component.util

data class AlarmItemAction(
    val onItemClick: () -> Unit,
    val onSwitchBtnClick: (Boolean) -> Unit
)
