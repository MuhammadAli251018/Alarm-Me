package com.muhammadali.alarmme.feature.main.ui.screen

import kotlinx.coroutines.flow.StateFlow

interface  UIStateManager <T> {
    val uiState: StateFlow<T>

    fun updateUIState(newState: T)
}