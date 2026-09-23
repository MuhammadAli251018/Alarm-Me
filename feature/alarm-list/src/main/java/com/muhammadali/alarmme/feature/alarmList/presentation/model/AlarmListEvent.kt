package com.muhammadali.alarmme.feature.alarmList.presentation.model

import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.mvi.UiEvent

sealed interface AlarmsListEvent : UiEvent {
    /** One row's switch was flipped. */
    data class OnEnableOrDisableAlarm(val id: AlarmId, val enabled: Boolean) : AlarmsListEvent

    /** The selection bar's bulk action — currently only ever used to disable. */
    data class OnEnableOrDisableAlarms(
        val ids: Set<AlarmId>,
        val enabled: Boolean
    ) : AlarmsListEvent

    data class OnDeleteAlarms(val ids: Set<AlarmId>) : AlarmsListEvent

    /** "Try again" from the error state: start observing the alarms over. */
    data object OnRetry : AlarmsListEvent
}
