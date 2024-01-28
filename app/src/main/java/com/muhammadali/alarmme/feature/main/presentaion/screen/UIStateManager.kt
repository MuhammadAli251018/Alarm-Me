package com.muhammadali.alarmme.feature.main.presentaion.screen

import kotlinx.coroutines.flow.StateFlow

interface  UIStateManager <T> {
    val uiState: StateFlow<T>

    fun updateUIState(newState: T)
}