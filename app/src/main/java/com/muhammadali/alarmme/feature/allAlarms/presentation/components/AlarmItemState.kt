package com.muhammadali.alarmme.feature.allAlarms.presentation.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.muhammadali.alarmme.common.data.alarms.AlarmEntity
import com.muhammadali.alarmme.common.domain.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.presentaion.util.toAnnotatedString

data class AlarmItemState(
    val id: Int,
    val title: String,
    val time: AnnotatedString,
    val repeat: Array<Boolean>,
    val isScheduled: Boolean,
) {
    companion object {
        val default = AlarmItemState(
            id = 0,
            title = "title",
            time = buildAnnotatedString { append("time") }, // Todo: make api to convert time to annotated string, and make default value = now.toAnnotatedString
            repeat = arrayOf(false, true,  true,  true,  true,  true,  true), // Todo: change to better representation
            isScheduled = true
        )
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmItemState

        if (title != other.title) return false
        if (time != other.time) return false
        if (!repeat.contentEquals(other.repeat)) return false
        if (isScheduled != other.isScheduled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + repeat.contentHashCode()
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

fun Alarm.toAlarmState() = AlarmItemState(
    id = id,
    title = TODO(),
    time = TODO(),
    repeat = TODO(),
    isScheduled = TODO()
)

fun AlarmEntity.toAlarmItemState(): AlarmItemState {
    return AlarmItemState(
        title = title,
        id = id,
        time = TimeAdapter.getDateTimeFromEpochMilli(time).toAnnotatedString(),
        isScheduled = scheduled,
        repeat = repeat.getRepeatFromStringFormat()
    )
}

fun List<AlarmEntity>.toListOfAlarmItems(): List<AlarmItemState> {
    return mutableListOf<AlarmItemState>().apply {
        this@toListOfAlarmItems.forEach {
            this.add(it.toAlarmItemState())
        }
    }
}
