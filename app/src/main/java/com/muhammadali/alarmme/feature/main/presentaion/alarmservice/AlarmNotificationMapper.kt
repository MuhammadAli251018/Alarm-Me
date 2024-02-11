package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmPreferences
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter


fun Alarm.toAlarmNotification(timeAdapter: TimeAdapter): AlarmNotification {
    val localTime = timeAdapter.getTimeFormat(time)
    val stringTime = "${localTime.hour} : ${localTime.minute}"

    return AlarmNotification(
        id = alarmId,
        title = title,
        time = stringTime,
        allowSnooze = preferences.snooze !is AlarmPreferences.Snooze.NoSnooze,
        ringtoneRef = preferences.ringtoneRef
    )
}