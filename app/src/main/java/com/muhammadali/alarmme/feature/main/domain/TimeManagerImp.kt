package com.muhammadali.alarmme.feature.main.domain

import com.muhammadali.alarmme.feature.main.ui.screen.util.Date
import com.muhammadali.alarmme.feature.main.ui.screen.util.Period
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time
import com.muhammadali.alarmme.feature.main.ui.screen.util.toMonth
import java.util.Calendar

class TimeManagerImp : TimeManager {


    override fun getTimeFormat(time: Long): Time {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis

        return Time(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE))
    }

    override fun getDateFormat(time: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis

        return Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH).toMonth(), calendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun getTimeInMillis(time: Time): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR, if (time.period == Period.PM) time.hours + 12 else time.hours)
        calendar.set(Calendar.MINUTE, time.minutes)

        return calendar.timeInMillis
    }

    fun getCurrentTimeFormat() = getTimeFormat(System.currentTimeMillis())
}