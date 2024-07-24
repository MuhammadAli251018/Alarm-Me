package com.muhammadali.alarmme.feature.main.domain

import android.content.Context
import android.net.Uri
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.common.Notifications

interface AlarmCreator {

    fun fireAlarm(
        context: Context,
        channelId: String = Notifications.ALARMS_CHANNEL_ID,
        time: String,
        title: String,
        sound: Uri,
        vibration: LongArray,
        enableSnooze: Boolean
    )

    fun cancelAlarm(
        notificationId: Int =
            AlarmConstants.ALARM_NOTIFICATION_ID,

    )

}