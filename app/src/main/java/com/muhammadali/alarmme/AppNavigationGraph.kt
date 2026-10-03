package com.muhammadali.alarmme

import com.muhammadali.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreen
import com.muhammadali.alarmme.feature.alarmList.presentation.AlarmListScreen
import com.muhammadali.alarmme.feature.alarmRinging.presentation.AlarmRingingScreen
import com.muhammadali.alarmme.feature.debugPanel.presentation.DebugPanelScreen
import com.muhammadali.alarmme.feature.settings.presentation.SettingsScreen
import com.muhammadali.alarmme.navigation.AlarmDetailsParams
import com.muhammadali.alarmme.navigation.AlarmDetailsScreenId
import com.muhammadali.alarmme.navigation.AlarmListScreenId
import com.muhammadali.alarmme.navigation.AlarmRingingParams
import com.muhammadali.alarmme.navigation.AlarmRingingScreenId
import com.muhammadali.alarmme.navigation.DebugPanelScreenId
import com.muhammadali.alarmme.navigation.NavigationGraph
import com.muhammadali.alarmme.navigation.RingtonePickerDestination
import com.muhammadali.alarmme.navigation.RingtonePickerScreenId
import com.muhammadali.alarmme.navigation.SettingsDestination
import com.muhammadali.alarmme.navigation.SettingsScreenId
import com.muhammadali.alarmme.navigation.DebugPanelDestination
import com.muhammadali.alarmme.navigation.requireParams
import com.muhammadali.alarmme.ui.screen.ringtonePicker.presentation.RingtonePickerScreen

/**
 * The one place that turns a ScreenId into a concrete Screen. :app is the only module allowed
 * to see every feature, so this mapping cannot live anywhere else.
 */
val AppNavigationGraph = NavigationGraph { destination ->
    when (destination.screenId) {
        AlarmListScreenId -> AlarmListScreen(
            settingsDestination = SettingsDestination,
        )

        AlarmDetailsScreenId -> AlarmDetailsScreen(
            params = destination.requireParams<AlarmDetailsParams>(),
            ringtonePickerDestination = RingtonePickerDestination,
        )

        AlarmRingingScreenId -> AlarmRingingScreen(
            params = destination.requireParams<AlarmRingingParams>(),
        )

        SettingsScreenId -> SettingsScreen(
            debugPanelDestination = DebugPanelDestination,
        )

        DebugPanelScreenId -> DebugPanelScreen()

        RingtonePickerScreenId -> RingtonePickerScreen()

        else -> error("Unknown screen ID: ${destination.screenId}")
    }
}
