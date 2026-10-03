package dev.alidroid.alarmme.utility.kotlinDateTime

import kotlinx.datetime.LocalTime

/** `"06:49"` — 12-hour, zero-padded hour. */
fun LocalTime.toClockLabel(): String {
    val hoursInTwelve = if (hour % 12 == 0) 12 else hour % 12
    return "${hoursInTwelve.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}
