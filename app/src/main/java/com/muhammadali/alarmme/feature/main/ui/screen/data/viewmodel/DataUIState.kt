package com.muhammadali.alarmme.feature.main.ui.screen.data.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

data class DataUIState(
    val ringingTime: AnnotatedString,
    val alarmTime: AnnotatedString,
    val date: String,
    val repeatPattern: AlarmRepeatingPattern,
    val alarmTitle: String,
    val ringToneName:String,
    val vibrationMode: String,
    val snoozeMode: String,
    val alarmTextFieldLabelContent: String,
    val alarmTextFieldLabelColor: Color
    )

data class AlarmRepeatingPattern (
    var monday: Boolean = true,
    var tuesday: Boolean = true,
    var wednesday: Boolean = true,
    var thursday: Boolean = true,
    var friday: Boolean = true,
    var saturday: Boolean = true,
    var sunday: Boolean = true,
) {

    companion object {
        fun ofString(repeat: String): AlarmRepeatingPattern {
            val pattern = AlarmRepeatingPattern()
            repeat.forEachIndexed { index, c ->
                pattern[index] = c == '0'
            }

            return pattern
        }
    }
    operator fun get(day: Int): Boolean {
        return when(day) {
            0 -> monday
            1 -> tuesday
            2 -> wednesday
            3 -> thursday
            4 -> friday
            5 -> saturday
            else -> sunday
        }
    }

    operator fun set(day: Int, newValue: Boolean): AlarmRepeatingPattern {
        return when(day) {
            0 -> copy(monday = newValue)
            1 -> copy(tuesday = newValue)
            2 -> copy(wednesday = newValue)
            3 -> copy(thursday = newValue)
            4 -> copy(friday = newValue)
            5 -> copy(saturday = newValue)
            else -> copy(sunday = newValue)
        }
    }

    fun toArray(): Array<Boolean> {
       return arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
    }

    override fun toString(): String {
        var repeatStr = ""
        this.toArray().forEach {
            repeatStr += if (it)
                '0'
            else
                '1'
        }
        return repeatStr
    }
}
