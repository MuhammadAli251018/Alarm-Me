package com.muhammadali.alarmme.feature.alarmList.presentation.model

import com.muhammadali.alarmme.alarms.domain.AlarmId
import kotlinx.datetime.LocalTime

/*
 * One row of the alarms list, as the UI needs it.
 *
 * [description] is the ready-to-show schedule line ("Mon–Fri · Standup in five"), derived from the
 * alarm's schedule and label by the mapper rather than stored — see `AlarmListMapper`.
 */
data class AlarmItemState(
    val id: AlarmId,
    val enabled: Boolean,
    val time: LocalTime,
    val description: String,
)
