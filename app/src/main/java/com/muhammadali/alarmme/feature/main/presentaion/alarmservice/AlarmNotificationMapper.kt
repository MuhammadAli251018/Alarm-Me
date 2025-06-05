package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter


fun Alarm.toAlarmNotification(timeAdapter: TimeAdapter): AlarmNotification {
    val localTime = timeAdapter.getTimeFormat(time)
    val stringTime = "${localTime.hour} : ${localTime.minute}"

    return AlarmNotification(
        id = id,
        title = title,
        time = stringTime,
        allowSnooze = preferences.snooze,
        ringtoneRef = preferences.ringtone.reference
    )
}