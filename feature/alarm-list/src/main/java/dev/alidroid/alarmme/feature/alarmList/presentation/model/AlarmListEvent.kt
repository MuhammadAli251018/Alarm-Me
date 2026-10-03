package dev.alidroid.alarmme.feature.alarmList.presentation.model

import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.mvi.UiEvent

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
