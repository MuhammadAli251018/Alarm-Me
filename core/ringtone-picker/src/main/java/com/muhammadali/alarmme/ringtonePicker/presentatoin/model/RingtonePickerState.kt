package com.muhammadali.alarmme.ringtonePicker.presentatoin.model

import com.muhammadali.alarmme.ringtonePicker.presentatoin.component.RingtoneItem
import com.muhammadali.alarmme.mvi.UiState

sealed interface RingtonePickerState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : RingtonePickerState

    data class Loaded(
        val ringtones: List<RingtoneItem>,
        val selectedRingtoneId: String?,
        val playingRingtoneId: String?,
    ) : RingtonePickerState

    data object Error : RingtonePickerState
}
