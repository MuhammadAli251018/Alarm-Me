package com.muhammadali.alarmme.feature.main.domain

import android.content.Context
import android.net.Uri
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.common.Notifications

interface AlarmNotificationCreator {

    companion object {
        const val alarmIdKey = "alarmId"
        const val alarmTitleKey = "alarmTitle"
        const val alarmTimeKey = "alarmTime"
        const val alarmSoundUriKey = "alarmSoundUri"
        const val alarmVibrationKey = "alarmVibration"
        const val alarmSnoozeKey = "alarmSnooze"

        const val alarmNotificationId = "alarm_notification_id"
    }

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