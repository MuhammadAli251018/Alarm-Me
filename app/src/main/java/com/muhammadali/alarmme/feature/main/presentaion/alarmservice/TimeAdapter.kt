package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

interface TimeAdapter {

    companion object {
        fun getDateTimeFromEpochMilli(time: Long): LocalTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toLocalTime()
        }
    }

    fun getTimeFormat(time: Long): LocalTime

    fun getDateFormat(time: Long): LocalDate

    fun getTimeInMillis(time: LocalDateTime): Long

    //fun getCurrentTimeFormat(): Time

}