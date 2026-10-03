package dev.alidroid.alarmme.alarms.domain

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

/**
 * How far ahead a weekly repeat looks: a full week, plus today — so an alarm whose time has
 * already passed today still resolves to the same weekday next week.
 */
private const val REPEAT_LOOKAHEAD_DAYS = 7

/**
 * The next moment this alarm's schedule would fire, or `null` when it never would: an empty
 * [AlarmSchedule.Repeat], or an [AlarmSchedule.CustomDates] whose dates have all passed.
 *
 * Ignores [Alarm.enabled] — a disabled alarm still has a schedule, and callers that care about
 * armed-ness filter on `enabled` themselves.
 */
fun Alarm.nextOccurrence(now: LocalDateTime): LocalDateTime? = when (val alarmSchedule = schedule) {
    is AlarmSchedule.Repeat -> nextRepeatOccurrence(alarmSchedule, now)

    is AlarmSchedule.CustomDates -> alarmSchedule.customDates
        .map { date -> LocalDateTime(date, time) }
        .filter { it > now }
        .minOrNull()
}

private fun Alarm.nextRepeatOccurrence(
    schedule: AlarmSchedule.Repeat,
    now: LocalDateTime,
): LocalDateTime? {
    if (schedule.repeatDays.isEmpty()) return null

    return (0..REPEAT_LOOKAHEAD_DAYS)
        .asSequence()
        .map { dayOffset -> now.date.plus(DatePeriod(days = dayOffset)) }
        .filter { it.dayOfWeek in schedule.repeatDays }
        .map { date -> LocalDateTime(date, time) }
        .firstOrNull { it > now }
}
