package com.muhammadali.alarmme.feature.main.ui.screen.util

data class MainScreenAction(
    val onAddBtnClick: () -> Unit,
    val onItemClick: (index: Int) -> Unit,
    val onItemSwitchBtnClick: (index: Int, isScheduled: Boolean) -> Unit
)
