package com.muhammadali.alarmme.feature.main.domain.entities

interface AlarmNotificator {
    fun fireAlarm(alarmNotification: AlarmNotification)

    fun cancelAlarm(alarmId: Int)
}