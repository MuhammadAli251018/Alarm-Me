package dev.alidroid.alarmme.feature.alarmRinging.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.feature.alarmRinging.domain.AlarmRingingUseCase
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingEffect
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingEvent
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.AlarmRingingState
import dev.alidroid.alarmme.feature.alarmRinging.presentation.model.toRingingUiState
import dev.alidroid.alarmme.mvi.BaseScreenModel
import dev.alidroid.alarmme.utility.flow.onFailureOrTimeout
import dev.alidroid.alarmme.utility.flow.onSuccess
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class AlarmRingingScreenModel(
    private val alarmId: AlarmId,
    private val alarmRingingUseCase: AlarmRingingUseCase,
) : BaseScreenModel<AlarmRingingState, AlarmRingingEvent, AlarmRingingEffect>(
    initialState = AlarmRingingState.Initial
) {

    private var observeJob: Job? = null

    init { loadRingingAlarm() }

    override fun handleEvent(event: AlarmRingingEvent) {
        when (event) {
            AlarmRingingEvent.OnRetry -> loadRingingAlarm()

            AlarmRingingEvent.OnDismiss -> screenModelScope.launch {
                alarmRingingUseCase.dismissRingingAlarm(alarmId)
                sendEffect(AlarmRingingEffect.Dismissed)
            }

            AlarmRingingEvent.OnSnoozeClick -> Unit
        }
    }

    private fun loadRingingAlarm() {
        observeJob?.cancel()
        _state.value = AlarmRingingState.Loading

        observeJob = screenModelScope.launch {
            alarmRingingUseCase.getRingingAlarm(alarmId).first()
                .onFailureOrTimeout { _state.value = AlarmRingingState.Error }
                .onSuccess { alarm ->
                    _state.value = alarm
                        .toRingingUiState(LocalDateTime.now().date)
                        .let(AlarmRingingState::Loaded)
                }
        }
    }
}
