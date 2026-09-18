package com.muhammadali.alarmme.feature.alarmDetails.data

import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetails
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsId
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlinx.datetime.DayOfWeek

/** Long enough for the loading skeleton to be seen, short enough not to be in the way. */
private val INITIAL_LOAD_DELAY: Duration = 600.milliseconds

/**
 * An [AlarmDetailsUseCase] that keeps everything in memory.
 *
 * It exists so the alarm details screen can be driven by hand — editing and saving all work —
 * while the real data layer is still missing. Nothing survives process death, and it is
 * deliberately the only place that knows that: swapping it for the real implementation in
 * `alarmDetailsModule` is the whole migration.
 */
class InMemoryAlarmDetailsUseCase(
    initialAlarmDetails: List<AlarmDetails> = sampleAlarmDetails(),
    private val initialLoadDelay: Duration = INITIAL_LOAD_DELAY,
) : AlarmDetailsUseCase {

    private val alarmDetails = MutableStateFlow(initialAlarmDetails.associateBy { it.id })

    override fun getAlarmDetails(id: AlarmDetailsId): Flow<AlarmDetails?> = flow {
        delay(initialLoadDelay)
        emitAll(alarmDetails.map { it[id] })
    }

    override suspend fun saveAlarmDetails(details: AlarmDetails) {
        alarmDetails.update { current -> current + (details.id to details) }
    }

    override suspend fun deleteAlarmDetails(id: AlarmDetailsId) {
        alarmDetails.update { current -> current - id }
    }
}

@OptIn(ExperimentalTime::class)
internal fun sampleAlarmDetails(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): List<AlarmDetails> {
    val now = Clock.System.now()

    return listOf(
        AlarmDetails(
            id = AlarmDetailsId("weekday"),
            label = "Weekday mornings",
            time = (now + 7.hours).toLocalDateTime(timeZone),
            repeatDays = setOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            ),
            customDateEnabled = false,
            customDates = emptyList(),
            soundName = "Sunrise chime",
            snoozeDurationMinutes = 9,
            snoozeCount = 3,
            gradualVolumeEnabled = true,
            vibrateEnabled = false,
        )
    )
}
