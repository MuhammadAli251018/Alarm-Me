package com.muhammadali.alarmme.feature.main.ui.screen.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadali.alarmme.feature.main.data.repo.DBRepository
import com.muhammadali.alarmme.feature.main.domain.AlarmScheduler
import com.muhammadali.alarmme.feature.main.ui.component.util.AlarmItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val alarmsDbRepository: DBRepository,
    private val alarmScheduler: AlarmScheduler,
    ) : ViewModel(), MainScreenPresenter
{


    init {
        viewModelScope.launch {
            alarmsDbRepository.getAllAlarms().collectLatest { alarms ->
                //updateUIState(MainUIState(alarms.toListOfAlarmItems()))
            }
        }
    }

    private val _alarms = MutableStateFlow<List<AlarmItemState>>(emptyList())
    override val alarms = _alarms.asStateFlow()

    override fun onAlarmItemClick(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onSwitchBtnAlarmItemClick(id: Int, scheduled: Boolean, context: Context) {
        updateAlarm(id, scheduled, context)
    }

    override fun onAddBtnClick() {
        TODO("Not yet implemented")
    }

    private fun updateAlarm(id: Int, scheduled: Boolean, context: Context) {
        viewModelScope.launch (Dispatchers.IO) {
            alarmsDbRepository.getAlarmById(id).collectLatest { alarm ->
                alarmsDbRepository.insertOrUpdateAlarm(alarm.copy(
                    scheduled = scheduled
                ))

                alarmsDbRepository.getFirstAlarmToRing().collectLatest {
                    alarmScheduler.scheduler.schedule(alarm = alarm, context = context)
                }
            }
        }
    }
}