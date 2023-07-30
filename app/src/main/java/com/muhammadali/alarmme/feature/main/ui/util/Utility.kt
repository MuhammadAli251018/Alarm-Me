package com.muhammadali.alarmme.feature.main.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.logging.SimpleFormatter


enum class TimeState(){
    Pm,
    Am;

    override fun toString() =
        if (this == TimeState.Pm)
            "PM"
        else
            "AM"
}



fun LocalTime.getTimeState(): TimeState =
    if (this.hour > 12)
        TimeState.Pm
    else
        TimeState.Am

fun LocalTime.getTwelveModeHours() =
    if (getTimeState() == TimeState.Pm)
        this.hour - 12
    else
        this.hour


fun LocalTime.toAnnotatedString(
    timeStyle: SpanStyle = SpanStyle(),
    periodStyle: SpanStyle = SpanStyle()
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(timeStyle) {
            append("${getTwelveModeHours()} : ${minute}")
        }

        withStyle(periodStyle) {
            append(getTimeState().toString())
        }
    }
}

fun LocalDate.toTextFormat(): String {
    return "$dayOfMonth ${getMonthPerf()} $year"
}

fun LocalDate.getMonthPerf(): String {
    return "${month.name[0]}${month.name[1]}${month.name[2]}"
}

infix fun LocalTime.minus(otherTime: LocalTime): LocalTime {
    var hours = this.hour - otherTime.hour
    var minutes = this.minute - otherTime.minute

    if (hours < 0)
        hours *= -1

    if (minutes < 0)
        minutes *= -1

    return LocalTime.of(hours, minutes)
}