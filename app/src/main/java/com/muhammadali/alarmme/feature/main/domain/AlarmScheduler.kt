package com.muhammadali.alarmme.feature.main.domain

import android.content.BroadcastReceiver
import android.content.Context
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.feature.main.data.Alarm

interface AlarmScheduler {
companion object {
    const val ALARM_TIME_KEY = "ALARM_TIME_KEY"
    const val ALARM_TITLE_KEY = "ALARM_TITLE_KEY"
    const val ALARM_DB_ID_KEY = "ALARM_DB_ID_KEY"
    const val ALARM_SOUND_URI_KEY = "ALARM_SOUND_URI_KEY"
    const val ALARM_VIBRATION_KEY = "ALARM_VIBRATION_KEY"
    const val ALARM_SNOOZE_KEY = "ALARM_SNOOZE_KEY"
}
    val scheduler: ScheduleAlarm
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