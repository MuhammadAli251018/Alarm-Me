package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.content.Context
import com.muhammadali.alarmme.feature.main.data.AlarmEntity

fun interface ScheduleAlarm {
    fun schedule(alarmEntity: AlarmEntity, context: Context)
}