package com.muhammadali.alarmme.ringtonePicker.presentatoin.model

import com.muhammadali.alarmme.alarms.domain.RingtoneId
import com.muhammadali.alarmme.mvi.UiState

sealed interface RingtonePickerState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : RingtonePickerState

    /*
     * [selectedRingtoneId] is the user's un-committed choice — it lives here rather than in storage
     * until Save hands it back as a navigation result, and starts null so Save stays disabled
     * until something is actually picked.
     */
    data class Loaded(
        val ringtones: List<RingtoneItemState>,
        val selectedRingtoneId: RingtoneId?,
        val playingRingtoneId: RingtoneId?,
    ) : RingtonePickerState

    data object Error : RingtonePickerState
}
