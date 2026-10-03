package dev.alidroid.alarmme.feature.alarmRinging.presentation.model

import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

data class AlarmRingingUiState(
    val id: AlarmId,
    val time: LocalTime,
    val label: String,
    val date: LocalDate,
    val phase: Phase,
    val snooze: Snooze,
) {

    sealed interface Phase {

        data object Ringing : Phase

        data class Snoozed(
            val nextRingAt: LocalTime,
            val remaining: Duration
        ) : Phase
    }

    sealed interface Snooze {

        data class Available(val duration: Duration, val remainingCount: Int) : Snooze

        data object Exhausted : Snooze
    }
}
