package com.muhammadali.alarmme.utility.kotlinDateTime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime {
    val currentMoment = Clock.System.now()
    val systemZone = TimeZone.currentSystemDefault()
    return currentMoment.toLocalDateTime(systemZone)
}

/** This time as epoch milliseconds, resolved in the system's current time zone. */
@OptIn(ExperimentalTime::class)
fun LocalDateTime.toEpochMilliseconds(): Long =
    toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

/** This time shifted forward by [duration], in the system's current time zone. */
@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.plus(duration: Duration): LocalDateTime {
    val zone = TimeZone.currentSystemDefault()
    return (toInstant(zone) + duration).toLocalDateTime(zone)
}

/** Elapsed time from [other] to this, in the system's current time zone — negative if [other] is later. */
@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.minus(other: LocalDateTime): Duration {
    val zone = TimeZone.currentSystemDefault()
    return toInstant(zone) - other.toInstant(zone)
}
