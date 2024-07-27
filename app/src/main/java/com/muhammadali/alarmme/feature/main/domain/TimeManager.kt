package com.muhammadali.alarmme.feature.main.domain

import com.muhammadali.alarmme.feature.main.ui.screen.util.Date
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time

interface TimeManager {

    fun getTimeFormat(time: Long): Time

    fun getDateFormat(time: Long): Date

    fun getTimeInMillis(time: Time): Long

    //fun getCurrentTimeFormat(): Time

}