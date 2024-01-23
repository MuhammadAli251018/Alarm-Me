package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import com.muhammadali.alarmme.common.AlarmConstants

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

fun Context.getAlarmScheduler(): AlarmScheduler {
    return AlarmSchedulerImp(
        getSystemService(ALARM_SERVICE) as AlarmManager,
        TimeAdapterImp()
    )
}