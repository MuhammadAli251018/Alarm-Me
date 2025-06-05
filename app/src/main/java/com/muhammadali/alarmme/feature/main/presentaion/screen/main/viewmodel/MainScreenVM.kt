package com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.allAlarms.presentation.components.AlarmItemState
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TAG = "MainScreenVMTag"
class MainScreenVM (
    private val alarmsDbRepository: AlarmsDBRepo,
    private val alarmScheduler: AlarmScheduler,
    private val timeAdapter: TimeAdapter,
    private val timeDateFormatter: TimeDateFormatter
    ) : ViewModel(), MainScreenPresenter
{
    init {
        viewModelScope.launch(Dispatchers.IO) {
           alarmsDbRepository.getAllAlarms().collectLatest { result ->
                //updateUIState(MainUIState(alarms.toListOfAlarmItems()))
               result.handleDataAsync(onSuccess = {storedAlarms ->
                   _alarms.emit(storedAlarms.map {
                       AlarmItemState(
                           it.alarmId,
                           title = it.title,
                           time = timeDateFormatter.formatAlarmTimeToAnnotatedString(timeAdapter.getTimeFormat(it.time)),
                           repeat = arrayOf(true, true, true,true, true, true, true),
                           isScheduled = it.enabled
                       )
                   })
               }, onFailure = {})
            }
        }
    }

    private val _alarms = MutableStateFlow<List<AlarmItemState>>(emptyList())
    override val alarms = _alarms.asStateFlow()

    override fun onAlarmItemClick(id: Int) {
        /*viewModelScope.launch(Dispatchers.IO){

        }*/
    }

    override fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean) {
        updateAlarm(id, scheduled)
        Log.d(TAG, "switch button click called")

    }

    override fun onAddBtnClick() {
        //TODO("Not yet implemented")
    }

    private fun updateAlarm(id: Int, scheduled: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { alarmsDbRepository.getAlarmWithId(id).first() }.await()

            result.handleData(
                    onSuccess = {
                        //  Todo handle errors

                        Log.d(TAG, "given id UI with id: ${id}, read id: ${it.alarmId}")

                        val updatedAlarm = it.copy(enabled = scheduled)

                        this.launch {
                            alarmsDbRepository.addOrUpdateAlarm(updatedAlarm)
                        }

                        if (scheduled)
                            alarmScheduler.scheduleOrUpdate(updatedAlarm).getOrElse { Log.d(TAG, "alarm scheduled successfully") }
                        else
                            alarmScheduler.cancelAlarm(updatedAlarm.alarmId).getOrElse {  Log.d(TAG, "alarm canceled successfully")}
                    },
                    onFailure = {

                    }
                )

        }
    }
}