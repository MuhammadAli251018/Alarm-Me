package dev.alidroid.alarmme.feature.alarmList.presentation.model

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.nextOccurrence
import dev.alidroid.alarmme.utility.kotlinDateTime.toGroupedLabel
import dev.alidroid.alarmme.utility.kotlinDateTime.toRangeLabel
import kotlinx.datetime.LocalDateTime

private const val DESCRIPTION_SEPARATOR = " · "

/**
 * The domain → state boundary for the alarms list.
 */
internal fun List<Alarm>.toLoadedState(now: LocalDateTime): AlarmsListState.Loaded =
    AlarmsListState.Loaded(
        alarms = map { it.toItemState() },
        // Only armed alarms are candidates for "next", and each contributes its own next firing
        // rather than a bare time-of-day, so tomorrow's 6am sorts after tonight's 11pm.
        nextAlarmTime = filter { it.enabled }.mapNotNull { it.nextOccurrence(now) }.minOrNull()
    )

internal fun Alarm.toItemState(): AlarmItemState = AlarmItemState(
    id = id,
    enabled = enabled,
    time = time,
    description = description()
)

/**
 * "Mon–Fri · Standup in five". Either half is dropped when it has nothing to say, so an unlabelled
 * everyday alarm reads "Every day" rather than "Every day · ", and an unscheduled one reads as just
 * its label.
 */
private fun Alarm.description(): String {
    val scheduleLabel = when (val alarmSchedule = schedule) {
        is AlarmSchedule.Repeat -> alarmSchedule.repeatDays.toRangeLabel()
        is AlarmSchedule.CustomDates -> alarmSchedule.customDates.toGroupedLabel()
    }

    return listOf(scheduleLabel, label)
        .filter { it.isNotBlank() }
        .joinToString(DESCRIPTION_SEPARATOR)
}
