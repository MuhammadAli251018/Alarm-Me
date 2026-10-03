package com.muhammadali.alarmme.navigation

import com.muhammadali.alarmme.alarms.domain.AlarmId
import kotlinx.parcelize.Parcelize

// ---- alarm list (root) ----
val AlarmListScreenId = ScreenId("alarms-list")
val AlarmListDestination = NavDestination(AlarmListScreenId)

// ---- alarm details ----
val AlarmDetailsScreenId = ScreenId("alarm-details")

/** null alarmId == "create a new alarm". */
@Parcelize
data class AlarmDetailsParams(val alarmId: AlarmId?) : NavigationParameter

fun alarmDetailsDestination(alarmId: AlarmId? = null) =
    NavDestination(AlarmDetailsScreenId, AlarmDetailsParams(alarmId))

// ---- alarm ringing ----
val AlarmRingingScreenId = ScreenId("alarm-ringing")

@Parcelize
data class AlarmRingingParams(val alarmId: AlarmId) : NavigationParameter

fun alarmRingingDestination(alarmId: AlarmId) =
    NavDestination(AlarmRingingScreenId, AlarmRingingParams(alarmId))

// ---- settings ----
val SettingsScreenId = ScreenId("settings")
val SettingsDestination = NavDestination(SettingsScreenId)

// ---- debug panel ----
val DebugPanelScreenId = ScreenId("debug-panel")
val DebugPanelDestination = NavDestination(DebugPanelScreenId)

// ---- ringtone picker ----
val RingtonePickerScreenId = ScreenId("ringtone-picker")
val RingtonePickerDestination = NavDestination(RingtonePickerScreenId)
