package com.muhammadali.alarmme.feature.main.ui.component.util

import androidx.compose.ui.text.AnnotatedString
import com.muhammadali.alarmme.feature.main.data.Alarm
import com.muhammadali.alarmme.feature.main.domain.TimeAdapter
import com.muhammadali.alarmme.feature.main.ui.util.toAnnotatedString

data class AlarmItemState(
    val alarmDBId: Int,
    val alarmTitle: String,
    val alarmTime: AnnotatedString,
    val alarmRepeat: Array<Boolean>,
    val isScheduled: Boolean,
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

fun String.getRepeatFromStringFormat(): Array<Boolean> {
    return mutableListOf<Boolean>().apply {
        this@getRepeatFromStringFormat.forEach {
            if (it == 0.toChar())
                this.add(true)
            else
                this.add(false)
        }
    }.toTypedArray()
}

fun Alarm.toAlarmItemState(): AlarmItemState {
    return AlarmItemState(
        alarmTitle = title,
        alarmDBId = id,
        alarmTime = TimeAdapter.getDateTimeFromEpochMilli(time).toAnnotatedString(),
        isScheduled = scheduled,
        alarmRepeat = repeat.getRepeatFromStringFormat()
    )
}

fun List<Alarm>.toListOfAlarmItems(): List<AlarmItemState> {
    return mutableListOf<AlarmItemState>().apply {
        this@toListOfAlarmItems.forEach {
            this.add(it.toAlarmItemState())
        }
    }
}
