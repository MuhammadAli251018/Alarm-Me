package dev.alidroid.alarmme.feature.alarmDetails.presentation.model

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmPreferences
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.SnoozePreferences
import dev.alidroid.alarmme.alarms.domain.nextOccurrence
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

/**
 * The domain → state boundary for alarm details. */
internal fun Alarm.toUiState(
    now: LocalDateTime,
    lengthOptions: List<Duration>,
    limitOptions: List<Int?>,
): AlarmDetailsUiState = AlarmDetailsUiState(
    id = id,
    label = label,
    time = time,
    nextOccurrence = nextOccurrence(now),
    repeatDays = (schedule as? AlarmSchedule.Repeat)?.repeatDays ?: emptySet(),
    customDateEnabled = schedule is AlarmSchedule.CustomDates,
    customDates = (schedule as? AlarmSchedule.CustomDates)?.customDates ?: emptyList(),
    ringtone = preferences.ringtone,
    snoozeLengthIndex = preferences.snoozePreferences?.let {
        lengthOptions.indexOfMinutes(it.snoozeDuration.inWholeMinutes.toInt())
    },
    snoozeLimitIndex = preferences.snoozePreferences?.let {
        limitOptions.indexOfLimit(it.snoozeCount)
    },
    gradualVolumeEnabled = preferences.gradualVolumeEnabled,
    vibrateEnabled = preferences.vibrationEnabled
)

/**
 * State → domain, for saving alarm details.
 */
internal fun AlarmDetailsUiState.toAlarm(
    isEnabled: Boolean,
    lengthOptions: List<Duration>,
    limitOptions: List<Int?>,
) = Alarm(
    id = this.id,
    enabled = isEnabled,
    label = label,
    time = time,
    schedule = if (customDateEnabled) {
        AlarmSchedule.CustomDates(customDates)
    } else {
        AlarmSchedule.Repeat(repeatDays)
    },
    preferences = AlarmPreferences(
        ringtone = ringtone,
        snoozePreferences = toSnoozePreferences(lengthOptions, limitOptions),
        gradualVolumeEnabled = gradualVolumeEnabled,
        vibrationEnabled = vibrateEnabled
    )
)

private fun AlarmDetailsUiState.toSnoozePreferences(
    lengthOptions: List<Duration>,
    limitOptions: List<Int?>,
): SnoozePreferences? {
    val lengthIndex = snoozeLengthIndex ?: return null
    val limitValue = snoozeLimitIndex?.let { limitOptions[it] } ?: Int.MAX_VALUE
    return SnoozePreferences(lengthOptions[lengthIndex], limitValue)
}

private fun List<Duration>.indexOfMinutes(minutes: Int): Int? =
    indexOfFirst { it.inWholeMinutes.toInt() == minutes }.takeIf { it >= 0 }

/** `Int.MAX_VALUE` is this app's "no limit" sentinel, matched to the `null` (∞) box. */
private fun List<Int?>.indexOfLimit(count: Int): Int? = when {
    count == Int.MAX_VALUE -> indexOf(null)
    else -> indexOf(count).takeIf { it >= 0 }
}
