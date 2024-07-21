package com.muhammadali.alarmme.feature.main.ui.screen.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit


enum class Period (val value: String) {
    AM("AM"),
    PM("PM")
}
data class Time(
    val hours: Int,
    val minutes: Int,
    val period: Period
) {
    /**
     * use for 24 format
     * */
    constructor(hours: Int, minutes: Int) : this(period = if (hours > 12) Period.PM else Period.AM,
    hours = if (hours > 12) hours - 12 else hours,
    minutes = minutes
    )
}

fun Time.toTextFormat(twentyForeFormat: Boolean = false, getOnlyTime: Boolean = false): String {

    return if (twentyForeFormat) {

        val hours = if(this.period == Period.PM) this.hours + 12 else this.hours

        "${if (hours >= 10) hours else "0$hours"} : ${
            when {
                minutes == 0 -> "00"
                minutes < 10 -> "0$minutes"
                else -> this.minutes

            }
        }"
    }
    else {
        "${if (hours >= 10) hours else "0$hours"} : ${
            when {
                minutes == 0 -> "00"
                minutes < 10 -> "0$minutes"
                else -> minutes

            }
        } " + if(!getOnlyTime) period.value else ""
    }

}


fun Time.toAnnotatedString(
    timeStyle: SpanStyle,
    periodStyle: SpanStyle
): AnnotatedString = buildAnnotatedString {
    withStyle(timeStyle) {
        append(toTextFormat(getOnlyTime = true))
    }

    withStyle(periodStyle) {
        append(period.value)
    }
}

//todo convert from & to long format
