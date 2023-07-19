package com.muhammadali.alarmme.feature.main.ui.screen.util

data class Time(
    val hours: Int,
    val minutes: Int,
)

//todo fix value when minutes is 0 -> 00

fun Time.toTextFormat(): String {
    return "${if (this.hours >= 10) this.hours else "0" + this.hours} : ${
        when {
            this.minutes == 0 -> "00"
            this.minutes < 10 -> "0" + this.minutes
            else -> this.minutes
            
        }
    }"
}

//todo convert from & to long format
