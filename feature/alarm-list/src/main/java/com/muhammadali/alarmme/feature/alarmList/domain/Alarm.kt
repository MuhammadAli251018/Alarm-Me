package com.muhammadali.alarmme.feature.alarmList.domain

import kotlinx.datetime.LocalDateTime

@JvmInline
value class AlarmItemId(val value: String)

/**
 * A scheduled alarm. [description] is the ready-to-show schedule line ("Mon–Fri · Standup in five")
 * rather than structured repeat data — the backing store is in-memory only, so there is nothing yet
 * that would need to recompute it.
 */
data class Alarm(
    val id: AlarmItemId,
    val enabled: Boolean,
    val time: LocalDateTime,
    val description: String,
)
