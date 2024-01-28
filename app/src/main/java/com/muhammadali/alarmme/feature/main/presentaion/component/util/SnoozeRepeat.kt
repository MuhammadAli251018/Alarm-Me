package com.muhammadali.alarmme.feature.main.presentaion.component.util

enum class SnoozeRepeat (val content: String) {
    ThreeTimes("3 times"),
    FiveTimes ("5 times"),
    InfiniteTimes("forever")
}

fun getAllSnoozeRepeat(): Array<SnoozeRepeat> = arrayOf(SnoozeRepeat.ThreeTimes, SnoozeRepeat.FiveTimes, SnoozeRepeat.InfiniteTimes)