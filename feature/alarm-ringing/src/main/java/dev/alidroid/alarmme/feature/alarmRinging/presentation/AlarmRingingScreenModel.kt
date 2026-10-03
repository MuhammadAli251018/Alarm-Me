package dev.alidroid.alarmme.feature.alarmRinging.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingEvent
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingState
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.toRingingUiState
import dev.alidroid.alarmme.mvi.BaseScreenModel
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class AlarmRingingScreenModel(
    private val alarmId: AlarmId,
    private val alarmRingingUseCase: AlarmRingingUseCase,
) : BaseScreenModel<AlarmRingingState, AlarmRingingEvent, Nothing>(
    initialState = AlarmRingingState.Initial
) {

    private var observeJob: Job? = null

    init { loadRingingAlarm() }

    override fun handleEvent(event: AlarmRingingEvent) {
        when (event) {
            AlarmRingingEvent.OnRetry -> loadRingingAlarm()

            // Nothing rings or snoozes yet, so neither action has anywhere to go.
            AlarmRingingEvent.OnSnoozeClick,
            AlarmRingingEvent.OnDismiss -> Unit
        }
    }

    private fun loadRingingAlarm() {
        observeJob?.cancel()
        _state.value = AlarmRingingState.Loading

        observeJob = screenModelScope.launch {
            alarmRingingUseCase.observeRingingAlarm(alarmId)
                .catch { _state.value = AlarmRingingState.Error }
                .collect { alarm ->
                    // An id with no alarm behind it has nothing to ring about.
                    _state.value = alarm
                        ?.toRingingUiState(LocalDateTime.now().date)
                        ?.let(AlarmRingingState::Loaded)
                        ?: AlarmRingingState.Error
                }
        }
    }
}
