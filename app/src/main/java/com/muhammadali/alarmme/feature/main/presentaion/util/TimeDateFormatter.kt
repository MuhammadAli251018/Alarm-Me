package com.muhammadali.alarmme.feature.main.presentaion.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class TimeDateFormatter @Inject constructor() {

    enum class Period(val strValur: String) {
        PM("PM"),
        AM("AM")
    }

    private fun getPeriod(hours: Int): Period {
        return when{
            hours > 12 -> Period.PM
            else -> Period.AM
        }
    }

    private fun getRingingFormat(
        hours: Int,
        minutes: Int,
        wordsStyle: SpanStyle,
        timeNumbersStyle: SpanStyle): AnnotatedString {
        //"Ringing after if(hour > = 0){((H) hour / hours) and} (M) (minute/ minutes)"

        val hoursIdentifier = if (hours > 1) "hours" else "hour"
        val minutesIdentifier = if (minutes > 1) "minute" else "minutes"

        val ringingPhrase = "Ringing after "
        val hourPhrase = if (hours > 0) "$hours $hoursIdentifier and" else ""
        val minutePhrases = "$minutes $minutesIdentifier"

        return buildAnnotatedString {
            withStyle(wordsStyle) {
                append(ringingPhrase)
            }

            withStyle(timeNumbersStyle) {
                append(hourPhrase)
                append(minutePhrases)
            }
        }
    }

    private fun getAlarmTimeFormat(
        hours: Int,
        minutes: Int,
        period: Period,
        timeStyle: SpanStyle,
        periodStyle: SpanStyle): AnnotatedString {
        //"if(hours < 10) {0<H>} else{<H>}:if(minutes < 10) {0<M>} else if(minutes == 0) {00} else{<M>}<P>"

        val hour = if (hours < 10) "0$hours" else "$hours"
        val minute = ":" +
                if (minutes == 0) "00"
                else if (minutes < 10) "0$minutes"
                else minutes

        return buildAnnotatedString {
            withStyle(timeStyle) {
                append(hour)
                append(minute)
            }

            withStyle(periodStyle) {
                append(period.strValur)
            }
        }
    }

    fun formatRingingTimeToAnnotatedString(hours: Int, minutes: Int): AnnotatedString {
        return getRingingFormat(
            hours,
            minutes,
            wordsStyle = SpanStyle(fontSize = 10.sp),
            timeNumbersStyle = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )

    }

    fun formatAlarmTimeToAnnotatedString(time: LocalTime): AnnotatedString {
        val period = getPeriod(time.hour)

        val hours = if ( period == Period.PM)
            time.hour -12
            else if (time.hour == 0)
                12
            else
                time.hour
        return  getAlarmTimeFormat(hours, time.minute, period,
            timeStyle = SpanStyle(fontSize = 50.sp),
            periodStyle = SpanStyle(fontSize = 20.sp,
                fontWeight = FontWeight.Bold))

    }

    fun getAlarmDateAsString(date: LocalDate): String {
        val month = getMonthShortName(date.month.name)

        return "${date.dayOfMonth} $month ${date.year}"
    }

    private fun getMonthShortName(monthName: String) = monthName[0].toString() + monthName[1] + monthName[2]

}
