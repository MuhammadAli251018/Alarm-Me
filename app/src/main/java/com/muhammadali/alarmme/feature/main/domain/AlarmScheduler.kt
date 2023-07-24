package com.muhammadali.alarmme.feature.main.domain

import android.content.BroadcastReceiver
import android.content.Context
import com.muhammadali.alarmme.common.AlarmConstants

interface AlarmScheduler {

    companion object {
        const val alarmIdKey = "alarmId"
        const val alarmTitleKey = "alarmTitle"
        const val alarmTimeKey = "alarmTime"
        const val alarmSoundUriKey = "alarmSoundUri"
        const val alarmVibrationKey = "alarmVibration"
        const val alarmSnoozeKey = "alarmSnooze"
    }

    fun setAlarm(
        time: Long,
        context: Context,
        receiver: Class<out BroadcastReceiver>,
        //alarm Data
        alarmDBId: Int,
        alarmTitle: String,
        alarmTime: String,
        alarmSoundUri: String,
        alarmVibration: Boolean,
        alarmSnooze: Boolean,
        alarmId: Int = AlarmConstants.ALARM_PENDING_INTENT_ID
    )

    fun cancelAlarm(
        context: Context,
        receiver: Class<out BroadcastReceiver>,
        alarmId: Int = AlarmConstants.ALARM_PENDING_INTENT_ID
    )
}