package com.muhammadali.alarmme.feature.main.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface TimeAdapter {

    fun getTimeFormat(time: Long): LocalTime

    fun getDateFormat(time: Long): LocalDate

    fun getTimeInMillis(time: LocalDateTime): Long

    //fun getCurrentTimeFormat(): Time

}