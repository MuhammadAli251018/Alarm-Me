package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.feature.debugPanel.domain.AlarmsWriteUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.FeatureFlagsUpdateUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ResetAppSettingsUseCase
import com.muhammadali.alarmme.feature.debugPanel.domain.ResetAppToFirstLaunchUseCase

/**
 * Wipes both preference stores the app declares — app settings and feature flags — and every
 * alarm. There is no onboarding flag yet; once one is added to either store, clearing it here
 * comes for free.
 */
class ResetAppToFirstLaunchUseCaseImpl(
    private val resetAppSettingsUseCase: ResetAppSettingsUseCase,
    private val featureFlagsUpdateUseCase: FeatureFlagsUpdateUseCase,
    private val alarmsWriteUseCase: AlarmsWriteUseCase,
) : ResetAppToFirstLaunchUseCase {

    override suspend fun reset() {
        resetAppSettingsUseCase.reset()
        featureFlagsUpdateUseCase.resetAllFlags()
        alarmsWriteUseCase.flushAlarms()
    }
}
