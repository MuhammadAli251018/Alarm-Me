package dev.alidroid.alarmme

import dev.alidroid.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreen
import dev.alidroid.alarmme.feature.alarmList.presentation.AlarmListScreen
import dev.alidroid.alarmme.feature.alarmRinging.presentation.AlarmRingingScreen
import dev.alidroid.alarmme.feature.debugPanel.presentation.DebugPanelScreen
import dev.alidroid.alarmme.feature.settings.presentation.SettingsScreen
import dev.alidroid.alarmme.navigation.AlarmDetailsParams
import dev.alidroid.alarmme.navigation.AlarmDetailsScreenId
import dev.alidroid.alarmme.navigation.AlarmListScreenId
import dev.alidroid.alarmme.navigation.AlarmRingingParams
import dev.alidroid.alarmme.navigation.AlarmRingingScreenId
import dev.alidroid.alarmme.navigation.DebugPanelScreenId
import dev.alidroid.alarmme.navigation.NavigationGraph
import dev.alidroid.alarmme.navigation.RingtonePickerDestination
import dev.alidroid.alarmme.navigation.RingtonePickerScreenId
import dev.alidroid.alarmme.navigation.SettingsDestination
import dev.alidroid.alarmme.navigation.SettingsScreenId
import dev.alidroid.alarmme.navigation.DebugPanelDestination
import dev.alidroid.alarmme.navigation.requireParams
import dev.alidroid.alarmme.ui.screen.ringtonePicker.presentation.RingtonePickerScreen

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
