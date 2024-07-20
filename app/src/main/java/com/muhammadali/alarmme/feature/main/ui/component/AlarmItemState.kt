package com.muhammadali.alarmme.feature.main.ui.component

import androidx.compose.ui.text.AnnotatedString

data class AlarmItemState(
    val alarmTitle: String,
    val alarmTime: AnnotatedString,
    val alarmRepeat: Array<Boolean>,
    val isScheduled: Boolean,
    val isEnabled: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmItemState

        if (alarmTitle != other.alarmTitle) return false
        if (alarmTime != other.alarmTime) return false
        if (!alarmRepeat.contentEquals(other.alarmRepeat)) return false
        if (isScheduled != other.isScheduled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = alarmTitle.hashCode()
        result = 31 * result + alarmTime.hashCode()
        result = 31 * result + alarmRepeat.contentHashCode()
        result = 31 * result + isScheduled.hashCode()
        return result
    }
}
