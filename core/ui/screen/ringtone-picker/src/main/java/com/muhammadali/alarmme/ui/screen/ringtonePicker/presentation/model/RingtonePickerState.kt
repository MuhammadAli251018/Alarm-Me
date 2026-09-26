package com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.model

import com.muhammadali.alarmme.alarms.domain.Ringtone
import com.muhammadali.alarmme.alarms.domain.RingtoneId
import com.muhammadali.alarmme.mvi.UiState

sealed interface RingtonePickerState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : RingtonePickerState

    /*
     * [selectedRingtone] is the user's un-committed choice — it lives here rather than in storage
     * until Save hands it back as a navigation result, and starts null so Save stays disabled
     * until something is actually picked. The whole tone, not just its id, so the caller gets
     * something it can use without looking anything up.
     */
    data class Loaded(
        val ringtones: List<RingtoneItemState>,
        val selectedRingtone: Ringtone?,
        val playingRingtoneId: RingtoneId?,
    ) : RingtonePickerState

    data object Error : RingtonePickerState
}
