package com.muhammadali.alarmme.feature.main.presentaion.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.muhammadali.alarmme.common.domain.AlarmPreferences
import com.muhammadali.alarmme.common.domain.getFromIndex
import java.time.LocalDate
import java.time.LocalTime


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

fun AlarmPreferences.RepeatPattern.toBooleanList(): List<Boolean> {
    val days = mutableListOf<Boolean>()

    for ( i in 0 until 7)
        days.add(activeDays.contains(getFromIndex(i)))

    return days
}