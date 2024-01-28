package com.muhammadali.alarmme.feature.main.presentaion.component.util


sealed class SnoozeState() {
    data class SnoozeMode (
        val repeat: SnoozeRepeat,
        val period: Int
    ) : SnoozeState()

    object SnoozeOff : SnoozeState()
}
