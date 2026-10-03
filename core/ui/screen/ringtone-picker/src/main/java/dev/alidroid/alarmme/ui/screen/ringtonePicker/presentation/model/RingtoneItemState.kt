package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model

import dev.alidroid.alarmme.alarms.domain.RingtoneId
import kotlin.time.Duration

/**
 * One row of the ringtone list, as the UI needs it. [duration] is null for a sound whose length
 * isn't known, in which case the row simply shows no meta line.
 */
data class RingtoneItemState(
    val id: RingtoneId,
    val title: String,
    val duration: Duration?,
)
