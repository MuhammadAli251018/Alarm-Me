package com.muhammadali.alarmme.feature.alarmDetails.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetails
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsId
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import com.muhammadali.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlarmDetailsScreenModel(
    private val alarmId: AlarmDetailsId,
    private val alarmDetailsUseCase: AlarmDetailsUseCase
) : BaseScreenModel<AlarmDetailsState, AlarmDetailsEvent, Nothing>(
    initialState = AlarmDetailsState.Initial
) {

    private var observeJob: Job? = null
    private var original: AlarmDetails? = null

    init {
        observeAlarmDetails()
    }

    override fun handleEvent(event: AlarmDetailsEvent) {
        when (event) {
            is AlarmDetailsEvent.OnDayToggle -> updateDraft { draft ->
                draft.copy(
                    repeatDays = if (draft.repeatDays.contains(event.day)) {
                        draft.repeatDays - event.day
                    } else {
                        draft.repeatDays + event.day
                    }
                )
            }

            is AlarmDetailsEvent.OnCustomDateToggle -> updateDraft { draft ->
                draft.copy(customDateEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnGradualVolumeToggle -> updateDraft { draft ->
                draft.copy(gradualVolumeEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnVibrateToggle -> updateDraft { draft ->
                draft.copy(vibrateEnabled = event.enabled)
            }

            // TODO: wire once the date picker / ringtone picker / snooze / label flows exist.
            AlarmDetailsEvent.OnCustomDateRowClick,
            AlarmDetailsEvent.OnSoundClick,
            AlarmDetailsEvent.OnSnoozeClick,
            AlarmDetailsEvent.OnLabelClick -> Unit

            AlarmDetailsEvent.OnSaveClick -> {
                val loaded = state.value as? AlarmDetailsState.Loaded ?: return
                screenModelScope.launch {
                    alarmDetailsUseCase.saveAlarmDetails(loaded.alarmDetails)
                }
            }

            AlarmDetailsEvent.OnDeleteClick -> screenModelScope.launch {
                alarmDetailsUseCase.deleteAlarmDetails(alarmId)
            }

            AlarmDetailsEvent.OnRetry -> observeAlarmDetails()
            AlarmDetailsEvent.OnAlarmTimeClick -> { /*TODO*/ }
        }
    }

    private inline fun updateDraft(crossinline reducer: (AlarmDetails) -> AlarmDetails) {
        updateStateIfInstanceOf<AlarmDetailsState.Loaded> {
            val draft = reducer(alarmDetails)
            copy(alarmDetails = draft, canSave = draft != original)
        }
    }

    /**
     * Restartable on purpose: the only way out of [AlarmDetailsState.Error] is to drop the failed
     * collection and subscribe again, which is what "Try again" does.
     */
    private fun observeAlarmDetails() {
        observeJob?.cancel()
        _state.value = AlarmDetailsState.Loading

        observeJob = screenModelScope.launch {
            alarmDetailsUseCase.getAlarmDetails(alarmId)
                .catch { _state.value = AlarmDetailsState.Error }
                .collect { details ->
                    original = details
                    _state.value = if (details == null) {
                        AlarmDetailsState.Error
                    } else {
                        AlarmDetailsState.Loaded(alarmDetails = details, canSave = false)
                    }
                }
        }
    }
}
