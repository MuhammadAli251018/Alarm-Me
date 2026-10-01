package com.muhammadali.alarmme.feature.debugPanel.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.muhammadali.alarmme.feature.debugPanel.presentation.component.DebugPanelTopBar
import com.muhammadali.alarmme.feature.debugPanel.presentation.model.DebugPanelEvent
import com.muhammadali.alarmme.feature.debugPanel.presentation.model.DebugPanelState
import com.muhammadali.alarmme.ui.component.AmBuildBanner
import com.muhammadali.alarmme.ui.component.AmDestructiveTextButton
import com.muhammadali.alarmme.ui.component.AmDisclosureRow
import com.muhammadali.alarmme.ui.component.AmFlagDisclosureRow
import com.muhammadali.alarmme.ui.component.AmLabeledSectionCard
import com.muhammadali.alarmme.ui.component.AmNavigationDisclosureRow
import com.muhammadali.alarmme.ui.component.AmQuickActionButton
import com.muhammadali.alarmme.ui.component.AmStatusPill
import com.muhammadali.alarmme.ui.component.AmTwoLineDisclosureRow
import com.muhammadali.alarmme.ui.component.ErrorScreenContent
import com.muhammadali.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeColors
import com.muhammadali.alarmme.ui.theme.LocalAlarmMeTypography

class DebugPanelScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel: DebugPanelScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent
        val navigator = LocalNavigator.current

        DebugPanelScreenContent(
            modifier = Modifier
                .imePadding()
                .systemBarsPadding()
                .fillMaxSize(),
            state = state,
            onBackClick = { navigator?.pop() },
            onRetry = { dispatch(DebugPanelEvent.OnRetry) },
            onGlobalMocksToggle = { dispatch(DebugPanelEvent.OnGlobalMocksToggle(it)) },
            onMockAlarmsToggle = { dispatch(DebugPanelEvent.OnMockAlarmsToggle(it)) },
            onMockRingtonesToggle = { dispatch(DebugPanelEvent.OnMockRingtonesToggle(it)) },
            onResetAllFlagsClick = { dispatch(DebugPanelEvent.OnResetAllFlagsClick) },
            onLaunchRingingScreenClick = { dispatch(DebugPanelEvent.OnLaunchRingingScreenClick) },
            onFireTestAlarmClick = { dispatch(DebugPanelEvent.OnFireTestAlarmClick(it)) },
            onSeedSampleAlarmsClick = { dispatch(DebugPanelEvent.OnSeedSampleAlarmsClick) },
            onScheduledAlarmsClick = { dispatch(DebugPanelEvent.OnScheduledAlarmsClick) },
            onFlushDatabaseClick = { dispatch(DebugPanelEvent.OnFlushDatabaseClick) },
            onClearPreferencesClick = { dispatch(DebugPanelEvent.OnClearPreferencesClick) },
            onResetOnboardingClick = { dispatch(DebugPanelEvent.OnResetOnboardingClick) }
        )
    }
}

@Composable
fun DebugPanelScreenContent(
    state: DebugPanelState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onGlobalMocksToggle: (Boolean) -> Unit,
    onMockAlarmsToggle: (Boolean) -> Unit,
    onMockRingtonesToggle: (Boolean) -> Unit,
    onResetAllFlagsClick: () -> Unit,
    onLaunchRingingScreenClick: () -> Unit,
    onFireTestAlarmClick: (delayLabel: String) -> Unit,
    onSeedSampleAlarmsClick: () -> Unit,
    onScheduledAlarmsClick: () -> Unit,
    onFlushDatabaseClick: () -> Unit,
    onClearPreferencesClick: () -> Unit,
    onResetOnboardingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        DebugPanelState.Loading -> Column(
            modifier = modifier
                .fillMaxSize()
                .background(LocalAlarmMeColors.current.background)
        ) {
            DebugPanelTopBar(onBackClick = onBackClick)
        }

        DebugPanelState.Error -> ErrorScreenContent(
            modifier = modifier.fillMaxSize(),
            errorHeading = "Couldn't load the debug panel",
            errorDescription = "Something went wrong reading debug state.",
            onRetry = onRetry,
            onDismiss = onBackClick
        )

        is DebugPanelState.Loaded -> DebugPanelScreenContent(
            modifier = modifier.fillMaxSize(),
            buildInfo = state.buildInfo,
            globalMocksEnabled = state.globalMocksEnabled,
            onGlobalMocksToggle = onGlobalMocksToggle,
            mockAlarmsEnabled = state.mockAlarmsEnabled,
            onMockAlarmsToggle = onMockAlarmsToggle,
            mockRingtonesEnabled = state.mockRingtonesEnabled,
            onMockRingtonesToggle = onMockRingtonesToggle,
            onResetAllFlagsClick = onResetAllFlagsClick,
            onLaunchRingingScreenClick = onLaunchRingingScreenClick,
            onFireTestAlarmClick = onFireTestAlarmClick,
            onSeedSampleAlarmsClick = onSeedSampleAlarmsClick,
            scheduledAlarmsCount = state.scheduledAlarmsCount,
            onScheduledAlarmsClick = onScheduledAlarmsClick,
            exactAlarmPermissionGranted = state.exactAlarmPermissionGranted,
            onFlushDatabaseClick = onFlushDatabaseClick,
            onClearPreferencesClick = onClearPreferencesClick,
            onResetOnboardingClick = onResetOnboardingClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun DebugPanelScreenContent(
    modifier: Modifier = Modifier,
    buildInfo: String,
    globalMocksEnabled: Boolean,
    onGlobalMocksToggle: (Boolean) -> Unit,
    mockAlarmsEnabled: Boolean,
    onMockAlarmsToggle: (Boolean) -> Unit,
    mockRingtonesEnabled: Boolean,
    onMockRingtonesToggle: (Boolean) -> Unit,
    onResetAllFlagsClick: () -> Unit,
    onLaunchRingingScreenClick: () -> Unit,
    onFireTestAlarmClick: (delayLabel: String) -> Unit,
    onSeedSampleAlarmsClick: () -> Unit,
    scheduledAlarmsCount: Int,
    onScheduledAlarmsClick: () -> Unit,
    exactAlarmPermissionGranted: Boolean,
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
                            text = "Global mocks",
                            description = "global_mocks_feature_flag",
                            checked = globalMocksEnabled,
                            onCheckedChange = onGlobalMocksToggle
                        )

                        1 -> AmFlagDisclosureRow(
                            text = "Mock alarms",
                            description = "mock_alarms_feature_flag",
                            checked = mockAlarmsEnabled,
                            onCheckedChange = onMockAlarmsToggle
                        )

                        2 -> AmFlagDisclosureRow(
                            text = "Mock ringtones",
                            description = "mock_ringtones_feature_flag",
                            checked = mockRingtonesEnabled,
                            onCheckedChange = onMockRingtonesToggle
                        )

                        else -> ResetAllFlagsRow(onClick = onResetAllFlagsClick)
                    }
                }
            }

            AmLabeledSectionCard(label = "Triggers") {
                sections(count = 3) { index ->
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
                sections(count = 2) { index ->
                    when (index) {
                        0 -> AmNavigationDisclosureRow(
                            text = "Scheduled alarms",
                            value = scheduledAlarmsCount.toString(),
                            onClick = onScheduledAlarmsClick
                        )

                        // TODO: Add the other permissions
                        else -> AmDisclosureRow(
                            text = "Exact-alarm permission",
                            onClick = null
                        ) {
                            AmStatusPill(
                                text = if (exactAlarmPermissionGranted) "GRANTED" else "DENIED",
                                color = if (exactAlarmPermissionGranted) colors.primary else colors.error
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
    var globalMocksEnabled by remember { mutableStateOf(true) }
    var mockAlarmsEnabled by remember { mutableStateOf(true) }
    var mockRingtonesEnabled by remember { mutableStateOf(false) }

    AlarmMeThemeProvider {
        DebugPanelScreenContent(
            buildInfo = "v1.0.0-debug · build 142 · a3f9c21",
            globalMocksEnabled = globalMocksEnabled,
            onGlobalMocksToggle = { globalMocksEnabled = it },
            mockAlarmsEnabled = mockAlarmsEnabled,
            onMockAlarmsToggle = { mockAlarmsEnabled = it },
            mockRingtonesEnabled = mockRingtonesEnabled,
            onMockRingtonesToggle = { mockRingtonesEnabled = it },
            onResetAllFlagsClick = {},
            onLaunchRingingScreenClick = {},
            onFireTestAlarmClick = {},
            onSeedSampleAlarmsClick = {},
            scheduledAlarmsCount = 7,
            onScheduledAlarmsClick = {},
            exactAlarmPermissionGranted = true,
            onFlushDatabaseClick = {},
            onClearPreferencesClick = {},
            onResetOnboardingClick = {},
            onBackClick = {}
        )
    }
}
