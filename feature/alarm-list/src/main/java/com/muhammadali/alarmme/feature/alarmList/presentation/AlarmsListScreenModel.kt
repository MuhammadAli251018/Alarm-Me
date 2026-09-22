package com.muhammadali.alarmme.feature.alarmList.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.feature.alarmList.domain.Alarm
import com.muhammadali.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import com.muhammadali.alarmme.feature.alarmList.presentation.component.AlarmItem
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListEvent
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListState
import com.muhammadali.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

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
                alarmsProvideUseCase.enableOrDisableAlarm(
                    event.ids.associateWith { event.enabled }
                )
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
                .collect { alarms -> _state.value = alarms.toLoadedState() }
        }
    }
}


private fun List<Alarm>.toLoadedState(): AlarmsListState.Loaded = AlarmsListState.Loaded(
    alarms = map { alarm ->
        AlarmItem(
            id = alarm.id,
            enabled = alarm.enabled,
            time = alarm.time,
            description = alarm.description
        )
    },
    nextAlarmTime = filter { it.enabled }.minOfOrNull { it.time }
)
