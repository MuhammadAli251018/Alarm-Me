package com.muhammadali.alarmme.ringtonePicker.presentatoin

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.alarms.domain.Ringtone
import com.muhammadali.alarmme.mvi.BaseScreenModel
import com.muhammadali.alarmme.ringtonePicker.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.ringtonePicker.presentatoin.model.RingtonePickerEvent
import com.muhammadali.alarmme.ringtonePicker.presentatoin.model.RingtonePickerState
import com.muhammadali.alarmme.ringtonePicker.presentatoin.model.toItemState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RingtonePickerScreenModel(
    private val ringtoneProvideUseCase: RingtoneProvideUseCase
) : BaseScreenModel<RingtonePickerState, RingtonePickerEvent, Nothing>(
    initialState = RingtonePickerState.Initial
) {

    private var observeJob: Job? = null
    private var catalog: List<Ringtone> = emptyList()

    init { loadRingtones() }

    override fun handleEvent(event: RingtonePickerEvent) {
        when (event) {
            is RingtonePickerEvent.OnRingtoneClick ->
                updateStateIfInstanceOf<RingtonePickerState.Loaded> {
                    copy(playingRingtoneId = if (playingRingtoneId == event.id) null else event.id)
                }

            // Selection stays here rather than going to storage: the caller only learns about it
            // if the user saves.
            is RingtonePickerEvent.OnRingtoneSelect ->
                updateStateIfInstanceOf<RingtonePickerState.Loaded> {
                    copy(selectedRingtone = catalog.firstOrNull { it.id == event.id })
                }

            RingtonePickerEvent.OnRetry -> loadRingtones()
        }
    }

    private fun loadRingtones() {
        observeJob?.cancel()
        _state.value = RingtonePickerState.Loading

        observeJob = screenModelScope.launch {
            ringtoneProvideUseCase.getRingtones()
                .catch { _state.value = RingtonePickerState.Error }
                .collect { ringtones ->
                    catalog = ringtones

                    // Carry the transient bits across a catalog refresh rather than resetting them.
                    val loaded = _state.value as? RingtonePickerState.Loaded

                    _state.value = RingtonePickerState.Loaded(
                        ringtones = ringtones.map { it.toItemState() },
                        selectedRingtone = loaded?.selectedRingtone,
                        playingRingtoneId = loaded?.playingRingtoneId
                    )
                }
        }
    }
}
