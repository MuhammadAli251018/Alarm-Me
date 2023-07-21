package com.muhammadali.alarmme.feature.main.ui.component.util


sealed class SnoozeState() {
    data class SnoozeMode (
        val repeat: SnoozeRepeat,
        val period: Int
    ) : SnoozeState()

    object SnoozeOff : SnoozeState()
}
