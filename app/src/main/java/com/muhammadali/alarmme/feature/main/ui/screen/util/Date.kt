package com.muhammadali.alarmme.feature.main.ui.screen.util

data class Date(
    val year: Int,
    val month: Month,
    val day: Int
)

fun Date.toTextFormat(): String = "${this.day}  ${this.month.getShortName()} ${this.year}"