package dev.alidroid.alarmme.feature.alarmDetails.domain

import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule

interface DefaultAlarmRepository {
    suspend fun getAlarmSettings(): AlarmPreferences
    suspend fun getAlarmSchedule(): AlarmSchedule
}