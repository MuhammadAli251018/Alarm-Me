package com.muhammadali.alarmme.feature.ringtonePicker.presentatoin

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.feature.ringtonePicker.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.model.RingtonePickerEvent
import com.muhammadali.alarmme.feature.ringtonePicker.presentatoin.model.RingtonePickerState
import com.muhammadali.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RingtonePickerScreenModel(
    private val ringtoneProvideUseCase: RingtoneProvideUseCase
) : BaseScreenModel<RingtonePickerState, RingtonePickerEvent, Nothing>(
    initialState = RingtonePickerState.Initial
) {

    private var observeJob: Job? = null

    init {
        observeRingtones()
    }

    override fun handleEvent(event: RingtonePickerEvent) {
        when (event) {
            is RingtonePickerEvent.OnRingtoneClick ->
                updateStateIfInstanceOf<RingtonePickerState.Loaded> {
                    copy(playingRingtoneId = if (playingRingtoneId == event.id) null else event.id)
                }

            is RingtonePickerEvent.OnRingtoneSelect -> screenModelScope.launch {
                ringtoneProvideUseCase.selectRingtone(event.id)
            }

            RingtonePickerEvent.OnRetry -> observeRingtones()
        }
    }

    private fun observeRingtones() {
        observeJob?.cancel()
        _state.value = RingtonePickerState.Loading
        observeJob = screenModelScope.launch {
            combine(
                ringtoneProvideUseCase.getRingtones(),
                ringtoneProvideUseCase.getSelectedRingtoneId()
            ) { ringtones, selectedRingtoneId -> ringtones to selectedRingtoneId }
                .catch { _state.value = RingtonePickerState.Error }
                .collect { (ringtones, selectedRingtoneId) ->
                    val playingRingtoneId = (_state.value as? RingtonePickerState.Loaded)?.playingRingtoneId
                    _state.value = RingtonePickerState.Loaded(ringtones, selectedRingtoneId, playingRingtoneId)
                }
        }
    }
}
