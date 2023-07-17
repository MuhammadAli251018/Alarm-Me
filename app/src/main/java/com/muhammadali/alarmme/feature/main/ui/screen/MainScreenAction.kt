package com.muhammadali.alarmme.feature.main.ui.screen

data class MainScreenAction(
    val onAddBtnClick: () -> Unit,
    val onItemClick: (index: Int) -> Unit,
    val onItemSwitchBtnClick: (isScheduled: Boolean) -> Unit
)
