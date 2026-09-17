package com.muhammadali.alarmme.utility.kotlinDateTime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime {
    val currentMoment = Clock.System.now()
    val systemZone = TimeZone.currentSystemDefault()
    return currentMoment.toLocalDateTime(systemZone)
}
