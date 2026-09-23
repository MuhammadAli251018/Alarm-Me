package com.muhammadali.alarmme.ringtonePicker.presentatoin.model

import com.muhammadali.alarmme.alarms.domain.Ringtone

/**
 * The domain → state boundary for the picker.
 */
internal fun Ringtone.toItemState(): RingtoneItemState = RingtoneItemState(
    id = id,
    title = name,
    duration = duration
)
