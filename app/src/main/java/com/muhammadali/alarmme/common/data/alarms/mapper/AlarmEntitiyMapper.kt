package com.muhammadali.alarmme.common.data.alarms.mapper

import com.muhammadali.alarmme.common.data.alarms.AlarmEntity
import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.common.domain.AlarmPreferences
import com.muhammadali.alarmme.common.domain.DaysOfWeeks
import com.muhammadali.alarmme.common.domain.getFromIndex
import java.util.regex.Pattern

// Todo edit to handle errors

fun AlarmEntity.toAlarm(): Alarm {
    val ringtone = ringtoneRef.split(",")

    val preferences = AlarmPreferences(
        snooze = this.snooze,
        vibration = this.vibration,
        ringtone = AlarmPreferences.AlarmRingtone(ringtone[0], ringtone[1]),
        repeat = convertToRepeat(this.repeat)

    )
    return Alarm(
        alarmId = this.id,
        title = this.title,
        time = this.time,
        enabled = this.scheduled,
        preferences = preferences
    )
}

fun Alarm.toAlarmEntity(): AlarmEntity {
    return AlarmEntity(
        id = alarmId,
        time = this.time,
        title = this.title,
        scheduled = this.enabled,
        snooze = this.preferences.snooze,
        vibration = this.preferences.vibration,
        ringtoneRef = "${this.preferences.ringtone.name},${preferences.ringtone.reference}",
        repeat = encodeRepeatToString(this.preferences.repeat)
    )
}

private fun encodeRepeatToString(repeat: AlarmPreferences.RepeatPattern): String {
        var encodedRepeat = "weekly,"

        for (i in 0 .. 6) {
           if ((repeat as AlarmPreferences.RepeatPattern.Weekly).activeDays.contains(getFromIndex(i)))
               encodedRepeat += "0"
            else
                encodedRepeat += "1"
        }

    return encodedRepeat
}

fun convertToRepeat(repeat: String): AlarmPreferences.RepeatPattern {
    val pattern = repeat.split(Pattern.compile(","))

    val weeklyPattern = mutableListOf<DaysOfWeeks>()

    pattern[1].forEachIndexed { index, c ->
       if (c == '0')
           weeklyPattern.add(getFromIndex(index))
    }

    return AlarmPreferences.RepeatPattern.Weekly(weeklyPattern.toSet())

}
