package dev.alidroid.alarmme.feature.debugPanel.presentation

import android.os.Parcelable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.alidroid.alarmme.feature.debugPanel.R
import dev.alidroid.alarmme.feature.debugPanel.presentation.component.DebugPanelTopBar
import dev.alidroid.alarmme.feature.debugPanel.presentation.model.DebugPanelEvent
import dev.alidroid.alarmme.feature.debugPanel.presentation.model.DebugPanelState
import dev.alidroid.alarmme.ui.component.AmBuildBanner
import dev.alidroid.alarmme.ui.component.AmDestructiveTextButton
import dev.alidroid.alarmme.ui.component.AmDisclosureRow
import dev.alidroid.alarmme.ui.component.AmFlagDisclosureRow
import dev.alidroid.alarmme.ui.component.AmLabeledSectionCard
import dev.alidroid.alarmme.ui.component.AmNavigationDisclosureRow
import dev.alidroid.alarmme.ui.component.AmQuickActionButton
import dev.alidroid.alarmme.ui.component.AmStatusPill
import dev.alidroid.alarmme.ui.component.AmTwoLineDisclosureRow
import dev.alidroid.alarmme.ui.component.ErrorScreenContent
import dev.alidroid.alarmme.ui.components.R as ComponentsR
import dev.alidroid.alarmme.ui.screenCategory.CommonScreenCategoriesPreview
import dev.alidroid.alarmme.ui.screenCategory.RtlPreview
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeColors
import dev.alidroid.alarmme.ui.theme.LocalAlarmMeTypography
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private val TEST_ALARM_DELAYS = listOf(
    "10s" to 10.seconds,
    "1m" to 1.minutes,
    "5m" to 5.minutes,
)

@Parcelize
class DebugPanelScreen : Screen, Parcelable {

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
    onFireTestAlarmClick: (Duration) -> Unit,
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
            errorHeading = stringResource(R.string.debug_panel_error_heading),
            errorDescription = stringResource(R.string.debug_panel_error_description),
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
    onFireTestAlarmClick: (Duration) -> Unit,
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

            AmLabeledSectionCard(label = stringResource(R.string.debug_panel_section_feature_flags)) {
                sections(count = 4) { index ->
                    when (index) {
                        0 -> AmFlagDisclosureRow(
                            text = stringResource(R.string.debug_panel_global_mocks),
                            description = "global_mocks_feature_flag",
                            checked = globalMocksEnabled,
                            onCheckedChange = onGlobalMocksToggle
                        )

                        1 -> AmFlagDisclosureRow(
                            text = stringResource(R.string.debug_panel_mock_alarms),
                            description = "mock_alarms_feature_flag",
                            checked = mockAlarmsEnabled,
                            enabled = globalMocksEnabled,
                            onCheckedChange = onMockAlarmsToggle
                        )

                        2 -> AmFlagDisclosureRow(
                            text = stringResource(R.string.debug_panel_mock_ringtones),
                            description = "mock_ringtones_feature_flag",
                            checked = mockRingtonesEnabled,
                            enabled = globalMocksEnabled,
                            onCheckedChange = onMockRingtonesToggle
                        )

                        else -> ResetAllFlagsRow(onClick = onResetAllFlagsClick)
                    }
                }
            }

            AmLabeledSectionCard(label = stringResource(R.string.debug_panel_section_triggers)) {
                sections(count = 3) { index ->
                    when (index) {
                        0 -> AmDisclosureRow(
                            text = stringResource(R.string.debug_panel_launch_ringing_screen),
                            onClick = null
                        ) {
                            AmQuickActionButton(text = stringResource(ComponentsR.string.common_run), onClick = onLaunchRingingScreenClick)
                        }

                        1 -> AmDisclosureRow(
                            text = stringResource(R.string.debug_panel_fire_test_alarm_in),
                            onClick = null
                        ) {
                            TEST_ALARM_DELAYS.forEach { (delayLabel, duration) ->
                                AmQuickActionButton(
                                    text = delayLabel,
                                    onClick = { onFireTestAlarmClick(duration) }
                                )
                            }
                        }

                        else -> AmDisclosureRow(
                            text = stringResource(R.string.debug_panel_seed_sample_alarms),
                            onClick = null
                        ) {
                            AmQuickActionButton(text = stringResource(ComponentsR.string.common_run), onClick = onSeedSampleAlarmsClick)
                        }
                    }
                }
            }

            AmLabeledSectionCard(label = stringResource(R.string.debug_panel_section_diagnostics)) {
                sections(count = 2) { index ->
                    when (index) {
                        0 -> AmNavigationDisclosureRow(
                            text = stringResource(R.string.debug_panel_scheduled_alarms),
                            value = scheduledAlarmsCount.toString(),
                            onClick = onScheduledAlarmsClick
                        )

                        // TODO: Add the other permissions
                        else -> AmDisclosureRow(
                            text = stringResource(R.string.debug_panel_exact_alarm_permission),
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
                label = stringResource(R.string.debug_panel_section_danger_zone),
                labelColor = colors.error,
                isDangerSection = true
            ) {
                sections(count = 3) { index ->
                    when (index) {
                        0 -> AmTwoLineDisclosureRow(
                            title = stringResource(R.string.debug_panel_flush_database_title),
                            subtitle = stringResource(R.string.debug_panel_flush_database_subtitle)
                        ) {
                            AmDestructiveTextButton(text = stringResource(R.string.debug_panel_flush), onClick = onFlushDatabaseClick)
                        }

                        1 -> AmTwoLineDisclosureRow(
                            title = stringResource(R.string.debug_panel_clear_preferences_title),
                            subtitle = stringResource(R.string.debug_panel_clear_preferences_subtitle)
                        ) {
                            AmDestructiveTextButton(text = stringResource(ComponentsR.string.common_clear), onClick = onClearPreferencesClick)
                        }

                        else -> AmTwoLineDisclosureRow(
                            title = stringResource(R.string.debug_panel_reset_onboarding_title),
                            subtitle = stringResource(R.string.debug_panel_reset_onboarding_subtitle)
                        ) {
                            AmDestructiveTextButton(text = stringResource(R.string.debug_panel_reset), onClick = onResetOnboardingClick)
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
            text = stringResource(R.string.debug_panel_reset_all_flags),
            style = LocalAlarmMeTypography.current.labelLarge,
            color = LocalAlarmMeColors.current.primary
        )
    }
}

@CommonScreenCategoriesPreview
@RtlPreview
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
