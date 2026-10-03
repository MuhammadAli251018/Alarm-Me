package dev.alidroid.alarmme.feature.debugPanel.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.alidroid.alarmme.feature.debugPanel.domain.AlarmsWriteUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ResetAppSettingsUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ResetAppToFirstLaunchUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ResolveExactAlarmPermissionUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ScheduleAlarmUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.ScheduledAlarmsCountUseCase
import dev.alidroid.alarmme.feature.debugPanel.domain.fireInstantAlarm
import dev.alidroid.alarmme.feature.debugPanel.presentation.model.DebugPanelEvent
import dev.alidroid.alarmme.feature.debugPanel.presentation.model.DebugPanelState
import dev.alidroid.alarmme.feature.debugPanel.presentation.model.toLoadedState
import dev.alidroid.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/** Matches the screen's "Seed 20 sample alarms" row. */
private const val SEEDED_ALARMS_COUNT = 20

class DebugPanelScreenModel(
    private val featureFlagsReadUseCase: FeatureFlagsReadUseCase,
    private val featureFlagsUpdateUseCase: FeatureFlagsUpdateUseCase,
    private val scheduledAlarmsCountUseCase: ScheduledAlarmsCountUseCase,
    private val resolveExactAlarmPermissionUseCase: ResolveExactAlarmPermissionUseCase,
    private val appBuildInfoUseCase: AppBuildInfoUseCase,
    private val alarmsWriteUseCase: AlarmsWriteUseCase,
    private val resetAppSettingsUseCase: ResetAppSettingsUseCase,
    private val resetAppToFirstLaunchUseCase: ResetAppToFirstLaunchUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : BaseScreenModel<DebugPanelState, DebugPanelEvent, Nothing>(
    initialState = DebugPanelState.Initial
) {

    private var observeJob: Job? = null

    init { loadDebugPanel() }

    override fun handleEvent(event: DebugPanelEvent) {
        when (event) {
            is DebugPanelEvent.OnGlobalMocksToggle -> screenModelScope.launch {
                featureFlagsUpdateUseCase.setGlobalMocks(event.enabled)
            }

            is DebugPanelEvent.OnMockAlarmsToggle -> screenModelScope.launch {
                featureFlagsUpdateUseCase.setMockAlarms(event.enabled)
            }

            is DebugPanelEvent.OnMockRingtonesToggle -> screenModelScope.launch {
                featureFlagsUpdateUseCase.setMockRingtones(event.enabled)
            }

            DebugPanelEvent.OnResetAllFlagsClick -> screenModelScope.launch {
                featureFlagsUpdateUseCase.resetAllFlags()
            }

            DebugPanelEvent.OnSeedSampleAlarmsClick -> screenModelScope.launch {
                alarmsWriteUseCase.writeMockAlarms(SEEDED_ALARMS_COUNT)
            }

            DebugPanelEvent.OnFlushDatabaseClick -> screenModelScope.launch {
                alarmsWriteUseCase.flushAlarms()
            }

            DebugPanelEvent.OnClearPreferencesClick -> screenModelScope.launch {
                resetAppSettingsUseCase.reset()
            }

            DebugPanelEvent.OnResetOnboardingClick -> screenModelScope.launch {
                resetAppToFirstLaunchUseCase.reset()
            }

            DebugPanelEvent.OnRetry -> loadDebugPanel()

            is DebugPanelEvent.OnFireTestAlarmClick -> screenModelScope.launch {
                scheduleAlarmUseCase.fireDebugAlarmIn(event.duration)
            }

            DebugPanelEvent.OnLaunchRingingScreenClick -> screenModelScope.launch {
                scheduleAlarmUseCase.fireInstantAlarm()
            }

            DebugPanelEvent.OnScheduledAlarmsClick -> Unit
        }
    }

    private fun loadDebugPanel() {
        observeJob?.cancel()
        _state.value = DebugPanelState.Loading

        observeJob = screenModelScope.launch {
            val buildInfo = appBuildInfoUseCase.getBuildInfo()
            val exactAlarmPermissionGranted = resolveExactAlarmPermissionUseCase.isGranted()

            combine(
                featureFlagsReadUseCase.observeFlags(),
                scheduledAlarmsCountUseCase.observeScheduledAlarmsCount(),
            ) { flags, scheduledAlarmsCount ->
                toLoadedState(
                    flags = flags,
                    scheduledAlarmsCount = scheduledAlarmsCount,
                    exactAlarmPermissionGranted = exactAlarmPermissionGranted,
                    buildInfo = buildInfo,
                )
            }
                .catch { _state.value = DebugPanelState.Error }
                .collect { loaded -> _state.value = loaded }
        }
    }
}
