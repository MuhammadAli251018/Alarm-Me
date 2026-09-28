package com.muhammadali.alarmme.feature.debugPanel.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammadali.alarmme.feature.debugPanel.presentation.component.DebugPanelTopBar
import com.muhammadali.alarmme.ui.component.AmBuildBanner
import com.muhammadali.alarmme.ui.component.AmDestructiveTextButton
import com.muhammadali.alarmme.ui.component.AmDisclosureRow
import com.muhammadali.alarmme.ui.component.AmFlagDisclosureRow
import com.muhammadali.alarmme.ui.component.AmLabeledSectionCard
import com.muhammadali.alarmme.ui.component.AmNavigationDisclosureRow
import com.muhammadali.alarmme.ui.component.AmQuickActionButton
import com.muhammadali.alarmme.ui.component.AmStatusPill
import com.muhammadali.alarmme.ui.component.AmToggleDisclosureRow
import com.muhammadali.alarmme.ui.component.AmTwoLineDisclosureRow
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

@Composable
fun DebugPanelScreenContent(
    modifier: Modifier = Modifier,
    buildInfo: String,
    customDateScheduleEnabled: Boolean,
    onCustomDateScheduleToggle: (Boolean) -> Unit,
    holdToDismissEnabled: Boolean,
    onHoldToDismissToggle: (Boolean) -> Unit,
    ringtonePickerV2Enabled: Boolean,
    onRingtonePickerV2Toggle: (Boolean) -> Unit,
    onResetAllFlagsClick: () -> Unit,
    onLaunchRingingScreenClick: () -> Unit,
    onFireTestAlarmClick: (delayLabel: String) -> Unit,
    onShowPermissionDialogClick: () -> Unit,
    onSeedSampleAlarmsClick: () -> Unit,
    scheduledAlarmsCount: Int,
    onScheduledAlarmsClick: () -> Unit,
    exactAlarmPermissionGranted: Boolean,
    verboseLoggingEnabled: Boolean,
    onVerboseLoggingToggle: (Boolean) -> Unit,
    onExportLogsClick: () -> Unit,
    onFlushDatabaseClick: () -> Unit,
    onClearPreferencesClick: () -> Unit,
    onResetOnboardingClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val colors = LocalAlarmMeColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        DebugPanelTopBar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AmBuildBanner(details = buildInfo)

            AmLabeledSectionCard(label = "Feature flags") {
                sections(count = 4) { index ->
                    when (index) {
                        0 -> AmFlagDisclosureRow(
                            text = "Custom-date schedule",
                            description = "flag.custom_dates",
                            checked = customDateScheduleEnabled,
                            onCheckedChange = onCustomDateScheduleToggle
                        )

                        1 -> AmFlagDisclosureRow(
                            text = "Hold-to-dismiss",
                            description = "flag.hold_dismiss",
                            checked = holdToDismissEnabled,
                            onCheckedChange = onHoldToDismissToggle
                        )

                        2 -> AmFlagDisclosureRow(
                            text = "New ringtone picker",
                            description = "flag.picker_v2",
                            checked = ringtonePickerV2Enabled,
                            onCheckedChange = onRingtonePickerV2Toggle
                        )

                        else -> ResetAllFlagsRow(onClick = onResetAllFlagsClick)
                    }
                }
            }

            AmLabeledSectionCard(label = "Triggers") {
                sections(count = 4) { index ->
                    when (index) {
                        0 -> AmDisclosureRow(
                            text = "Launch ringing screen",
                            onClick = null
                        ) {
                            AmQuickActionButton(text = "Run", onClick = onLaunchRingingScreenClick)
                        }

                        1 -> AmDisclosureRow(
                            text = "Fire test alarm in",
                            onClick = null
                        ) {
                            listOf("10s", "1m", "5m").forEach { delayLabel ->
                                AmQuickActionButton(
                                    text = delayLabel,
                                    onClick = { onFireTestAlarmClick(delayLabel) }
                                )
                            }
                        }

                        2 -> AmDisclosureRow(
                            text = "Show permission dialog",
                            onClick = null
                        ) {
                            AmQuickActionButton(text = "Run", onClick = onShowPermissionDialogClick)
                        }

                        else -> AmDisclosureRow(
                            text = "Seed 20 sample alarms",
                            onClick = null
                        ) {
                            AmQuickActionButton(text = "Run", onClick = onSeedSampleAlarmsClick)
                        }
                    }
                }
            }

            AmLabeledSectionCard(label = "Diagnostics") {
                sections(count = 4) { index ->
                    when (index) {
                        0 -> AmNavigationDisclosureRow(
                            text = "Scheduled alarms",
                            value = scheduledAlarmsCount.toString(),
                            onClick = onScheduledAlarmsClick
                        )

                        1 -> AmDisclosureRow(
                            text = "Exact-alarm permission",
                            onClick = null
                        ) {
                            AmStatusPill(
                                text = if (exactAlarmPermissionGranted) "GRANTED" else "DENIED",
                                color = if (exactAlarmPermissionGranted) colors.primary else colors.error
                            )
                        }

                        2 -> AmToggleDisclosureRow(
                            text = "Verbose logging",
                            checked = verboseLoggingEnabled,
                            onCheckedChange = onVerboseLoggingToggle
                        )

                        else -> AmDisclosureRow(
                            text = "Export logs",
                            onClick = onExportLogsClick
                        ) {
                            Text(
                                text = "↗",
                                style = LocalAlarmMeTypography.current.bodyLarge,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            AmLabeledSectionCard(
                label = "Danger zone",
                labelColor = colors.error,
                isDangerSection = true
            ) {
                sections(count = 3) { index ->
                    when (index) {
                        0 -> AmTwoLineDisclosureRow(
                            title = "Flush alarm database",
                            subtitle = "Deletes every alarm"
                        ) {
                            AmDestructiveTextButton(text = "Flush", onClick = onFlushDatabaseClick)
                        }

                        1 -> AmTwoLineDisclosureRow(
                            title = "Clear preferences",
                            subtitle = "Settings back to first launch"
                        ) {
                            AmDestructiveTextButton(text = "Clear", onClick = onClearPreferencesClick)
                        }

                        else -> AmTwoLineDisclosureRow(
                            title = "Reset onboarding",
                            subtitle = "Show first-run flow again"
                        ) {
                            AmDestructiveTextButton(text = "Reset", onClick = onResetOnboardingClick)
                        }
                    }
                }
            }
        }
    }
}

/** A left-aligned, primary-colored action row — no title/trailing split, just one tap target. */
@Composable
private fun ResetAllFlagsRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Reset all flags",
            style = LocalAlarmMeTypography.current.labelLarge,
            color = LocalAlarmMeColors.current.primary
        )
    }
}

@CommonScreenCategoriesPreview
@Composable
private fun DebugPanelScreenContentPreview() {
    var customDateScheduleEnabled by remember { mutableStateOf(true) }
    var holdToDismissEnabled by remember { mutableStateOf(true) }
    var ringtonePickerV2Enabled by remember { mutableStateOf(false) }
    var verboseLoggingEnabled by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        DebugPanelScreenContent(
            buildInfo = "v1.0.0-debug · build 142 · a3f9c21",
            customDateScheduleEnabled = customDateScheduleEnabled,
            onCustomDateScheduleToggle = { customDateScheduleEnabled = it },
            holdToDismissEnabled = holdToDismissEnabled,
            onHoldToDismissToggle = { holdToDismissEnabled = it },
            ringtonePickerV2Enabled = ringtonePickerV2Enabled,
            onRingtonePickerV2Toggle = { ringtonePickerV2Enabled = it },
            onResetAllFlagsClick = {},
            onLaunchRingingScreenClick = {},
            onFireTestAlarmClick = {},
            onShowPermissionDialogClick = {},
            onSeedSampleAlarmsClick = {},
            scheduledAlarmsCount = 7,
            onScheduledAlarmsClick = {},
            exactAlarmPermissionGranted = true,
            verboseLoggingEnabled = verboseLoggingEnabled,
            onVerboseLoggingToggle = { verboseLoggingEnabled = it },
            onExportLogsClick = {},
            onFlushDatabaseClick = {},
            onClearPreferencesClick = {},
            onResetOnboardingClick = {},
            onBackClick = {}
        )
    }
}
