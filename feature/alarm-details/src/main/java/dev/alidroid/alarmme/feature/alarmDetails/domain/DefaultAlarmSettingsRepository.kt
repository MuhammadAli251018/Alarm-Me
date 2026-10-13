package dev.alidroid.alarmme.feature.alarmDetails.domain

import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import kotlinx.datetime.LocalTime

interface DefaultAlarmRepository {
    suspend fun getAlarmSettings(): AlarmPreferences
    fun getAlarmSchedule(): AlarmSchedule
    fun getAlarmTime(): LocalTime
}

fun DefaultAlarmRepository.getAlarmTimeAndSchedule(): Pair<LocalTime, AlarmSchedule> {
    return getAlarmTime() to getAlarmSchedule()
}