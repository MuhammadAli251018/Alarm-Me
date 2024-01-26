package com.muhammadali.alarmme.feature.main.data.local

import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmPreferences
import com.muhammadali.alarmme.feature.main.domain.entities.getFromIndex
import java.util.regex.Pattern

// Todo edit to handle errors

fun AlarmEntity.toAlarm(): Alarm {
    val preferences = AlarmPreferences(
        snooze = convertToSnoozeMode(this.snooze),
        vibration = this.vibration,
        ringtoneRef = this.ringtoneRef,
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
        time = this.time,
        title = this.title,
        scheduled = this.enabled,
        snooze = this.preferences.snooze.id,
        vibration = this.preferences.vibration,
        ringtoneRef = this.preferences.ringtoneRef,
        repeat = encodeRepeatToString(this.preferences.repeat)
    )
}

private fun encodeRepeatToString(repeat: AlarmPreferences.RepeatPattern): String {
    var encodedRepeat = ""
    if (repeat is AlarmPreferences.RepeatPattern.Weekly) {
        encodedRepeat += "weekly,"

        for (i in 0 .. 6) {
           if ((repeat as AlarmPreferences.RepeatPattern.Weekly).activeDays.contains(getFromIndex(i)))
               encodedRepeat += "0"
            else
                encodedRepeat += "1"
        }
    }
    else {
        encodedRepeat += "days,"
        (repeat as AlarmPreferences.RepeatPattern.CertainDays).days.forEach {
            encodedRepeat += it.toString()
        }
    }

    return encodedRepeat
}

fun convertToRepeat(repeat: String): AlarmPreferences.RepeatPattern {
    val pattern = repeat.split(Pattern.compile(","))

    return when(pattern[0]) {
        "weekly" -> {
            val weeklyPattern = mutableListOf<AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks>()

            pattern[1].forEachIndexed { index, c ->
               if (c == '0')
                   weeklyPattern.add(getFromIndex(index))
            }

            AlarmPreferences.RepeatPattern.Weekly(weeklyPattern)
        }
        else -> {
            val days = mutableListOf<Long>()
            for (i in 1 .. pattern.size)
                days.add(pattern[i].toLong())

            return AlarmPreferences.RepeatPattern.CertainDays(days)
        }
    }
}

private fun convertToSnoozeMode(snooze: Int): AlarmPreferences.Snooze {
    return when (snooze) {
        0 -> AlarmPreferences.Snooze.NoSnooze
        1 -> AlarmPreferences.Snooze.ThreeR5M
        else -> AlarmPreferences.Snooze.FiveR5M
    }
}