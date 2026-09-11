package com.muhammadali.alarmme.feature.alarmList.presentation

import com.muhammadali.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListEvent
import com.muhammadali.alarmme.feature.alarmList.presentation.model.AlarmsListState
import com.muhammadali.alarmme.mvi.BaseScreenModel

class AlarmsListScreenModel(
    private val alarmsProvideUseCase: AlarmsProvideUseCase
) : BaseScreenModel<AlarmsListState, AlarmsListEvent, Nothing>(
    initialState = AlarmsListState.Initial
) {

    override fun handleEvent(event: AlarmsListEvent) {
        TODO("Not yet implemented")
    }
}
