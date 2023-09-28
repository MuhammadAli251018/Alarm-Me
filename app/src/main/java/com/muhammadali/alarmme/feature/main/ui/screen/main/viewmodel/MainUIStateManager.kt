package com.muhammadali.alarmme.feature.main.ui.screen.main.viewmodel

import com.muhammadali.alarmme.feature.main.ui.screen.UIStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainUIStateManager(initialValue: MainUIState) : UIStateManager<MainUIState> {

    private val _uiState by lazy { MutableStateFlow(initialValue) }
    override val uiState by lazy { _uiState.asStateFlow() }

    override fun updateUIState(newState: MainUIState) {
        _uiState.update { newState }
    }

}