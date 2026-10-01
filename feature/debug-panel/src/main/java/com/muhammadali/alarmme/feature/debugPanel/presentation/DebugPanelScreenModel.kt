package com.muhammadali.alarmme.feature.debugPanel.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.feature.debugPanel.domain.AppBuildInfoUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsReadUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ResolveExactAlarmPermissionUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ScheduledAlarmsCountUseCase
import com.muhammadali.alarmme.feature.debugPanel.presentation.model.DebugPanelEvent
import com.muhammadali.alarmme.feature.debugPanel.presentation.model.DebugPanelState
import com.muhammadali.alarmme.feature.debugPanel.presentation.model.toLoadedState
import com.muhammadali.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DebugPanelScreenModel(
    private val featureFlagsReadUseCase: FeatureFlagsReadUseCase,
    private val featureFlagsUpdateUseCase: FeatureFlagsUpdateUseCase,
    private val scheduledAlarmsCountUseCase: ScheduledAlarmsCountUseCase,
    private val resolveExactAlarmPermissionUseCase: ResolveExactAlarmPermissionUseCase,
    private val appBuildInfoUseCase: AppBuildInfoUseCase,
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

            DebugPanelEvent.OnRetry -> loadDebugPanel()

            // No real destination/integration exists yet for any of these.
            is DebugPanelEvent.OnFireTestAlarmClick,
            DebugPanelEvent.OnLaunchRingingScreenClick,
            DebugPanelEvent.OnSeedSampleAlarmsClick,
            DebugPanelEvent.OnScheduledAlarmsClick,
            DebugPanelEvent.OnFlushDatabaseClick,
            DebugPanelEvent.OnClearPreferencesClick,
            DebugPanelEvent.OnResetOnboardingClick -> Unit
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
