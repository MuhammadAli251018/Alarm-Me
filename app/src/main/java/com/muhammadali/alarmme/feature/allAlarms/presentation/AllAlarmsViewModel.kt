package com.muhammadali.alarmme.feature.allAlarms.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.common.presentation.BaseViewModel
import com.muhammadali.alarmme.feature.allAlarms.presentation.components.AlarmItemState
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsEffect
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsEvent
import com.muhammadali.alarmme.feature.allAlarms.presentation.models.AllAlarmsState
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "AllAlarmsViewModel"

class AllAlarmsViewModel(
    // Todo: Make use cases for Adding/Editing alarm, so it groups updating the db and the system at the same time
    private val alarmsDbRepository: AlarmsDBRepo,
    private val alarmScheduler: AlarmScheduler,
    private val timeAdapter: TimeAdapter,
    private val timeDateFormatter: TimeDateFormatter
) : BaseViewModel<AllAlarmsState, AllAlarmsEvent, AllAlarmsEffect> (
    initialState = AllAlarmsState.default
) {

    init {
        viewModelScope.launch {
            alarmsDbRepository.getAllAlarms().collectLatest {
                it.fold(
                    onSuccess = { newAlarms ->
                        // Todo: instead of map create mapper functions and convert timeAdapter and timeDateFormater to Extension Functions
                        updateState {
                            copy(
                                alarms = newAlarms.map { alarm ->
                                    AlarmItemState(
                                        alarm.id,
                                        title = alarm.title,
                                        time = timeDateFormatter.formatAlarmTimeToAnnotatedString(timeAdapter.getTimeFormat(alarm.time)),
                                        repeat = arrayOf(true, true, true,true, true, true, true),
                                        isScheduled = alarm.enabled
                                    )
                                }
                            )
                        }
                    },
                    onFailure = {
                        // Todo: Push an effect to show an error message that the app couldn't load alarms
                    }
                )
            }
        }
    }

    override fun handleEvent(event: AllAlarmsEvent) {
        when(event) {
            AllAlarmsEvent.AddAlarmEvent -> handleAddAlarm()
            is AllAlarmsEvent.AlarmClickedEvent -> handleEditAlarm(event.index)
            is AllAlarmsEvent.AlarmEnableOrDisableEvent -> handleEnableOrDisableAlarm(event.index)
        }
    }

    private fun handleAddAlarm() {
        viewModelScope.launch {
            pushEffect(AllAlarmsEffect.NavigateToNewAlarmEffect)
        }
    }

    private fun handleEditAlarm(index: Int) {
        viewModelScope.launch {
            pushEffect(AllAlarmsEffect.NavigateToEditAlarmEffect(index))
        }
    }

    private fun handleEnableOrDisableAlarm(index: Int) {
        // Todo: update the alarm in the db and remove it from schedule
        viewModelScope.launch {
            val alarmItemState = state.value.alarms[index]
            val newScheduledState = !alarmItemState.isScheduled

            val alarmResult = async { alarmsDbRepository.getAlarmWithId(alarmItemState.id).first() }.await()

            alarmResult.fold(
                onSuccess = { alarm ->
                    updateAlarmAndSchedule(alarm, newScheduledState)
                },
                onFailure = { throwable ->
                    Log.e(TAG, "Failed to get alarm with id: ${alarmItemState.id}", throwable)
                    // Todo: Push an effect to show an error message that the app couldn't load the alarm (Might mean that the alarm id is wrong)
                }
            )
        }
    }

    private fun updateAlarmAndSchedule(alarmEntity: Alarm, scheduled: Boolean) {
        val updatedAlarm = alarmEntity.copy(enabled = scheduled)

        viewModelScope.launch {
            withContext(Dispatchers.IO) { alarmsDbRepository.addOrUpdateAlarm(updatedAlarm) }
        }

        if (scheduled) {
            alarmScheduler.scheduleOrUpdate(updatedAlarm)
                .onSuccess().also { Log.d(TAG, "Alarm (id: ${updatedAlarm.id}) scheduled successfully") }
                //.onFailure { Log.e(TAG, "Failed to schedule alarm (id: ${updatedAlarm.id})", it) }
        } else {
            alarmScheduler.cancelAlarm(updatedAlarm.id)
                .onSuccess().also { Log.d(TAG, "Alarm (id: ${updatedAlarm.id}) canceled successfully") }
                //.onFailure { Log.e(TAG, "Failed to cancel alarm (id: ${updatedAlarm.id})", it) }
        }
    }
}