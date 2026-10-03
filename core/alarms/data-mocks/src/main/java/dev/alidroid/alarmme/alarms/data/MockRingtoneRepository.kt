package dev.alidroid.alarmme.alarms.data

import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private val INITIAL_LOAD_DELAY: Duration = 600.milliseconds


class MockRingtoneRepository(
    initialRingtones: List<Ringtone> = sampleRingtones(),
    private val initialLoadDelay: Duration = INITIAL_LOAD_DELAY,
) : RingtoneRepository {

    private val ringtones = MutableStateFlow(initialRingtones.associateBy { it.id })

    override fun observeRingtones(): Flow<List<Ringtone>> = flow {
        delay(initialLoadDelay)
        emitAll(ringtones.map { it.values.toList() })
    }

    override fun observeRingtone(id: RingtoneId): Flow<Ringtone?> = flow {
        delay(initialLoadDelay)
        emitAll(ringtones.map { it[id] })
    }

    override fun getDefaultRingtone(): Ringtone {
        return ringtones.value.values.first()
    }
}

internal fun sampleRingtones(): List<Ringtone> = listOf(
    Ringtone(
        id = RingtoneId("sunrise-chime"),
        name = "Sunrise Chime",
        uri = "content://dev.alidroid.alarmme.ringtones/sunrise-chime",
        duration = 42.seconds,
    ),
    Ringtone(
        id = RingtoneId("morning-birds"),
        name = "Morning Birds",
        uri = "content://dev.alidroid.alarmme.ringtones/morning-birds",
        duration = 130.seconds,
    ),
    Ringtone(
        id = RingtoneId("gentle-waves"),
        name = "Gentle Waves",
        uri = "content://dev.alidroid.alarmme.ringtones/gentle-waves",
        duration = 90.seconds,
    ),
    Ringtone(
        id = RingtoneId("crystal-bells"),
        name = "Crystal Bells",
        uri = "content://dev.alidroid.alarmme.ringtones/crystal-bells",
        duration = 64.seconds,
    ),
    Ringtone(
        id = RingtoneId("soft-piano"),
        name = "Soft Piano",
        uri = "content://dev.alidroid.alarmme.ringtones/soft-piano",
        duration = null,
    ),
)