package com.muhammadali.alarmme.common.util

import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class TimeAdapterImp : TimeAdapter {


    override fun getTimeFormat(time: Long): LocalTime {
        val time = getLocalDateTimeFromEpoch(time)

        return time.toLocalTime()
    }

    private fun getLocalDateTimeFromEpoch(time: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
    }

    override fun getDateFormat(time: Long): LocalDate {
        val timeDate = getLocalDateTimeFromEpoch(time)
        return timeDate.toLocalDate()
    }

    override fun getTimeInMillis(time: LocalDateTime): Long {
        return time.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()
    }

    fun getCurrentTimeFormat() = getTimeFormat(System.currentTimeMillis())
}