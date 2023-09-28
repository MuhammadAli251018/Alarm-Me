package com.muhammadali.alarmme.feature.main.domain

import android.content.Context
import com.muhammadali.alarmme.feature.main.data.Alarm

fun interface ScheduleAlarm {
    fun schedule(alarm: Alarm, context: Context)
}