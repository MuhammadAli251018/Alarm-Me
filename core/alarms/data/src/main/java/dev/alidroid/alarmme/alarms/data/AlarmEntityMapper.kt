package dev.alidroid.alarmme.alarms.data

import dev.alidroid.alarmme.alarms.data.db.AlarmEntity
import dev.alidroid.alarmme.alarms.data.db.AlarmScheduleType
import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.domain.SnoozePreferences
import dev.alidroid.alarmme.alarms.domain.getRingtone
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.seconds

private const val LIST_SEPARATOR = ","

fun Alarm.toEntity(): AlarmEntity {
    val (scheduleType, repeatDays, customDates) = when (val alarmSchedule = schedule) {
        is AlarmSchedule.Repeat -> Triple(
            AlarmScheduleType.REPEAT,
            alarmSchedule.repeatDays.joinToString(LIST_SEPARATOR) { it.name },
            "",
        )

        is AlarmSchedule.CustomDates -> Triple(
            AlarmScheduleType.CUSTOM_DATES,
            "",
            alarmSchedule.customDates.joinToString(LIST_SEPARATOR) { it.toString() },
        )
    }

    return AlarmEntity(
        id = id.value,
        enabled = enabled,
        label = label,
        timeIso = time.toString(),
        scheduleType = scheduleType,
        repeatDays = repeatDays,
        customDates = customDates,
        ringtoneId = preferences.ringtone.id.value,
        snoozeDurationSeconds = preferences.snoozePreferences?.snoozeDuration?.inWholeSeconds,
        snoozeCount = preferences.snoozePreferences?.snoozeCount,
        vibrationEnabled = preferences.vibrationEnabled,
        gradualVolumeEnabled = preferences.gradualVolumeEnabled,
    )
}

suspend fun AlarmEntity.toDomain(ringtoneRepository: RingtoneRepository): Alarm {
    val schedule = when (scheduleType) {
        AlarmScheduleType.CUSTOM_DATES -> AlarmSchedule.CustomDates(customDates.toDateList())
        else -> AlarmSchedule.Repeat(repeatDays.toDayOfWeekSet())
    }

    val ringtone = ringtoneRepository.getRingtone(RingtoneId(ringtoneId))
        ?: ringtoneRepository.getDefaultRingtone()

    val snoozePreferences = snoozeDurationSeconds?.let { durationSeconds ->
        SnoozePreferences(
            snoozeDuration = durationSeconds.seconds,
            snoozeCount = snoozeCount ?: 0,
        )
    }

    return Alarm(
        id = AlarmId(id),
        enabled = enabled,
        label = label,
        time = LocalTime.parse(timeIso),
        schedule = schedule,
        preferences = AlarmPreferences(
            ringtone = ringtone,
            snoozePreferences = snoozePreferences,
            vibrationEnabled = vibrationEnabled,
            gradualVolumeEnabled = gradualVolumeEnabled,
        ),
    )
}

private fun String.toDayOfWeekSet(): Set<DayOfWeek> =
    if (isEmpty()) emptySet() else split(LIST_SEPARATOR).map { DayOfWeek.valueOf(it) }.toSet()

private fun String.toDateList(): List<LocalDate> =
    if (isEmpty()) emptyList() else split(LIST_SEPARATOR).map { LocalDate.parse(it) }
