package dev.alidroid.alarmme.feature.alarmDetails.presentation.fake

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

fun testAlarm(
    id: AlarmId = AlarmId.random(),
    enabled: Boolean = true,
    label: String = "Wake up",
    time: LocalTime = LocalTime(7, 0),
    schedule: AlarmSchedule = AlarmSchedule.Repeat(DayOfWeek.MONDAY),
    preferences: AlarmPreferences = AlarmPreferences(
        ringtone = Ringtone(RingtoneId("default"), "Default", "uri", null),
        snoozePreferences = null,
        vibrationEnabled = false,
        gradualVolumeEnabled = false,
    ),
) = Alarm(id, enabled, label, time, schedule, preferences)
