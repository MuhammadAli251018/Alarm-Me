package dev.alidroid.alarmme.feature.alarmList.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.alidroid.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import dev.alidroid.alarmme.feature.alarmList.presentation.model.AlarmsListEvent
import dev.alidroid.alarmme.feature.alarmList.presentation.model.AlarmsListState
import dev.alidroid.alarmme.feature.alarmList.presentation.model.toLoadedState
import dev.alidroid.alarmme.mvi.BaseScreenModel
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class AlarmsListScreenModel(
    private val alarmsProvideUseCase: AlarmsProvideUseCase
) : BaseScreenModel<AlarmsListState, AlarmsListEvent, Nothing>(
    initialState = AlarmsListState.Initial
) {

    private var observeJob: Job? = null

    init { loadAlarms() }

    override fun handleEvent(event: AlarmsListEvent) {
        when (event) {
            is AlarmsListEvent.OnEnableOrDisableAlarm -> screenModelScope.launch {
                alarmsProvideUseCase.enableOrDisableAlarm(event.id, event.enabled)
            }

            is AlarmsListEvent.OnEnableOrDisableAlarms -> screenModelScope.launch {
                alarmsProvideUseCase.enableOrDisableAlarms(event.ids, event.enabled)
            }

            is AlarmsListEvent.OnDeleteAlarms -> screenModelScope.launch {
                alarmsProvideUseCase.deleteAlarms(event.ids)
            }

            AlarmsListEvent.OnRetry -> loadAlarms()
        }
    }

    private fun loadAlarms() {
        observeJob?.cancel()
        _state.value = AlarmsListState.Loading

        observeJob = screenModelScope.launch {
            alarmsProvideUseCase.getAlarms()
                .catch { _state.value = AlarmsListState.Error }
                .collect { alarms -> _state.value = alarms.toLoadedState(LocalDateTime.now()) }
        }
    }
}
