package com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.component.util.AlarmItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val alarmsDbRepository: AlarmsDBRepo,
    private val alarmScheduler: AlarmScheduler,
    ) : ViewModel(), MainScreenPresenter
{


    init {
        viewModelScope.launch(Dispatchers.IO) {
           alarmsDbRepository.getAllAlarms().collectLatest { alarms ->
                //updateUIState(MainUIState(alarms.toListOfAlarmItems()))
            }
        }
    }

    private val _alarms = MutableStateFlow<List<AlarmItemState>>(emptyList())
    override val alarms = _alarms.asStateFlow()

    override fun onAlarmItemClick(id: Int) {
        /*viewModelScope.launch(Dispatchers.IO){

        }*/
    }

    override fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean, context: Context) {
        updateAlarm(id, scheduled, context)
    }

    override fun onAddBtnClick() {
        //TODO("Not yet implemented")
    }

    private fun updateAlarm(id: Int, scheduled: Boolean, context: Context) {
        viewModelScope.launch (Dispatchers.IO) {
            alarmsDbRepository.getAlarmWithId(id).collectLatest { alarm ->
                alarm.handleData(
                    onSuccess = {
                        //  Todo handle errors

                        val updatedAlarm = it.copy(enabled = scheduled)
                        this.launch {
                            alarmsDbRepository.addOrUpdateAlarm(updatedAlarm)
                        }

                        if (scheduled)
                            alarmScheduler.scheduleOrUpdate(updatedAlarm).handleData({}, {})
                    },
                    onFailure = {

                    }
                )
            }
        }
    }
}