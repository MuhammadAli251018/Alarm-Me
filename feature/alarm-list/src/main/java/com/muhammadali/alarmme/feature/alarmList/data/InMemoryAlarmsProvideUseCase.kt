package com.muhammadali.alarmme.feature.alarmList.data

import com.muhammadali.alarmme.feature.alarmList.domain.Alarm
import com.muhammadali.alarmme.feature.alarmList.domain.AlarmItemId
import com.muhammadali.alarmme.feature.alarmList.domain.AlarmsProvideUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

/** Long enough for the loading skeleton to be seen, short enough not to be in the way. */
private val INITIAL_LOAD_DELAY: Duration = 600.milliseconds

/**
 * An [AlarmsProvideUseCase] that keeps everything in memory.
 *
 * It exists so the alarms screen can be driven by hand — toggling, bulk-disabling and deleting all
 * work — while the real data layer is still missing. Nothing survives process death, and it is
 * deliberately the only place that knows that: swapping it for the real implementation in
 * `alarmListModule` is the whole migration.
 */
class InMemoryAlarmsProvideUseCase(
    initialAlarms: List<Alarm> = sampleAlarms(),
    private val initialLoadDelay: Duration = INITIAL_LOAD_DELAY,
) : AlarmsProvideUseCase {

    private val alarms = MutableStateFlow(initialAlarms)

    override fun getAlarms(): Flow<List<Alarm>> = flow {
        delay(initialLoadDelay)
        emitAll(alarms)
    }

    override suspend fun enableOrDisableAlarm(id: AlarmItemId, enabled: Boolean) =
        enableOrDisableAlarm(mapOf(id to enabled))

    override suspend fun enableOrDisableAlarm(items: Map<AlarmItemId, Boolean>) {
        alarms.update { current ->
            current.map { alarm ->
                val enabled = items[alarm.id]
                if (enabled == null) alarm else alarm.copy(enabled = enabled)
            }
        }
    }

    override suspend fun deleteAlarms(ids: List<AlarmItemId>) {
        val removed = ids.toSet()
        alarms.update { current -> current.filterNot { it.id in removed } }
    }
}

/**
 * Seeded relative to now rather than at fixed dates, so the top bar's "IN 7H 26M" countdown reads
 * sensibly whenever the app is launched.
 */
@OptIn(ExperimentalTime::class)
internal fun sampleAlarms(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): List<Alarm> {
    val now = Clock.System.now()

    fun at(offset: Duration): LocalDateTime = (now + offset).toLocalDateTime(timeZone)

    return listOf(
        Alarm(AlarmItemId("gym"), true, at(7.hours + 26.minutes), "Mon, Wed, Fri · Gym — leave by six"),
        Alarm(AlarmItemId("weekday"), true, at(9.hours), "Mon–Fri · Weekday mornings, gently"),
        Alarm(AlarmItemId("standup"), true, at(12.hours + 15.minutes), "Mon–Fri · Standup in five"),
        Alarm(AlarmItemId("meds"), true, at(15.hours), "Every day"),
        Alarm(AlarmItemId("bread"), false, at(18.hours + 5.minutes), "Thu 10 Sep · Take the bread out"),
        Alarm(
            id = AlarmItemId("laundry"),
            enabled = false,
            time = at(21.hours + 20.minutes),
            description = "Sun · Move the laundry over before it sits too long"
        ),
        Alarm(AlarmItemId("bins"), true, at(26.hours + 10.minutes), "Wed · Bins out"),
        Alarm(AlarmItemId("winddown"), false, at(30.hours + 45.minutes), "Every day · Wind down, screens off"),
        Alarm(AlarmItemId("kettle"), true, at(33.hours + 30.minutes), "Every day · Kettle off"),
    )
}
