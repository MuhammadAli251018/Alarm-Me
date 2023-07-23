package com.muhammadali.alarmme.feature.main.domain

import android.content.Context
import android.net.Uri
import com.muhammadali.alarmme.common.Notifications
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time

interface NotificationCreator {

    fun fireAlarm(
        context: Context,
        channelId: String = Notifications.ALARMS_CHANNEL_ID,
        time: Time,
        title: String,
        sound: Uri,
        vibration: LongArray,
        enableSnooze: Boolean
    )

}