package com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.model

import com.muhammadali.alarmme.alarms.domain.Ringtone

/**
 * The domain → state boundary for the picker.
 */
internal fun Ringtone.toItemState(): RingtoneItemState = RingtoneItemState(
    id = id,
    title = name,
    duration = duration
)
