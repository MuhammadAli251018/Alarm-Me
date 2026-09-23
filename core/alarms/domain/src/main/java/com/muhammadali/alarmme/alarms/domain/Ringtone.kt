package com.muhammadali.alarmme.alarms.domain

import kotlin.time.Duration

@JvmInline
value class RingtoneId(val value: String)

data class Ringtone(
    val id: RingtoneId,
    val name: String,
    val uri: String,
    val duration: Duration?
)