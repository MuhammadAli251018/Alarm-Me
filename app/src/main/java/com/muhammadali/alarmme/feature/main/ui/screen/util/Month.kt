package com.muhammadali.alarmme.feature.main.ui.screen.util

enum class Month(name: String) {
    January("January"),
    February("February"),
    March("March"),
    April("April"),
    May("May"),
    June("June"),
    July("July"),
    August("August"),
    September("September"),
    October("October"),
    November("November"),
    December("December")
}

fun Month.getNumber(): Int {
    return when(this) {
        Month.January -> 1
        Month.February -> 2
        Month.March -> 3
        Month.April -> 4
        Month.May -> 5
        Month.June -> 6
        Month.July -> 7
        Month.August -> 8
        Month.September -> 9
        Month.October -> 10
        Month.November -> 11
        Month.December -> 12
    }
}

fun Month.getShortName(): String =  this.name[0].toString() + this.name[1] + this.name[2]

fun Int.toMonth(): Month = when(this) {
    1 -> Month.January
    2 -> Month.February
    3 -> Month.March
    4 -> Month.April
    5 -> Month.May
    6 -> Month.June
    7 -> Month.July
    8 -> Month.August
    9 -> Month.September
    10 -> Month.October
    11 -> Month.November
    else -> Month.December
}