package dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.model

import dev.alidroid.alarmme.alarms.domain.Ringtone

/**
 * The domain → state boundary for the picker.
 */
internal fun Ringtone.toItemState(): RingtoneItemState = RingtoneItemState(
    id = id,
    title = name,
    duration = duration
)
