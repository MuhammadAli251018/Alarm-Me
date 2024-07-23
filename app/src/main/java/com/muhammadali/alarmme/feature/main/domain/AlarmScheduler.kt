package com.muhammadali.alarmme.feature.main.domain

import android.content.BroadcastReceiver
import android.content.Context
import com.muhammadali.alarmme.common.AlarmConstants

interface AlarmScheduler {

    fun setAlarm(
        time: Long,
        context: Context,
        receiver: Class<out BroadcastReceiver>,
        alarmId: Int = AlarmConstants.ALARM_PENDING_INTENT_ID
    )

    fun cancelAlarm(
        context: Context,
        receiver: Class<out BroadcastReceiver>,
        alarmId: Int = AlarmConstants.ALARM_PENDING_INTENT_ID
    )
}