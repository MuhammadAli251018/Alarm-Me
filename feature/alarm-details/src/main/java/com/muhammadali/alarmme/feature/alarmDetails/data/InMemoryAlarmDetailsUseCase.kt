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
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** Long enough for the loading skeleton to be seen, short enough not to be in the way. */
private val INITIAL_LOAD_DELAY: Duration = 600.milliseconds


@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
private fun AlarmDetails.Companion.new(): AlarmDetails = AlarmDetails(
    id = AlarmDetailsId(Uuid.random().toString()),
    label = "Untitled Alarm",
    time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    repeatDays = emptySet(),
    customDateEnabled = false,
    customDates = emptyList(),
    soundName = "",
    snoozeDurationMinutes = 0,
    snoozeCount = 2,
    gradualVolumeEnabled = false,
    vibrateEnabled = true,
)

class InMemoryAlarmDetailsUseCase(
    initialAlarmDetails: List<AlarmDetails> = sampleAlarmDetails(),
    private val initialLoadDelay: Duration = INITIAL_LOAD_DELAY,
) : AlarmDetailsUseCase {

    private val alarmDetails = MutableStateFlow(initialAlarmDetails.associateBy { it.id })
    private var draftAlarmDetails = MutableStateFlow(emptyList<AlarmDetails>().associateBy { it.id })

    override fun getAlarmDetails(id: AlarmDetailsId): Flow<Result<AlarmDetails>> = flow {
        delay(initialLoadDelay)
        emitAll(alarmDetails.map { runCatching { it[id]!! } })
    }

    override suspend fun draftNewAlarm(): Flow<AlarmDetails> {
        var id: AlarmDetailsId? = null

        draftAlarmDetails.update { draftAlarms ->
            val newAlarm = AlarmDetails.new().also { id = it.id }
            draftAlarms + (newAlarm.id to newAlarm)
        }
        return draftAlarmDetails.mapNotNull { it[id] }
    }

    override suspend fun saveAlarmDetails(details: AlarmDetails) {
        alarmDetails.update { current -> current + (details.id to details) }

        if (details.id in draftAlarmDetails.value) {
            draftAlarmDetails.update { current -> current - details.id }
        }
    }

    override suspend fun deleteAlarmDetails(id: AlarmDetailsId) {
        draftAlarmDetails.update { current -> current - id }
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
