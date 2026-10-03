package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.feature.debugPanel.domain.ResolveExactAlarmPermissionUseCase

/** Mocked: real resolution needs Android's AlarmManager/Context, not available in this layer yet. */
class ResolveExactAlarmPermissionUseCaseImpl : ResolveExactAlarmPermissionUseCase {

    override suspend fun isGranted(): Boolean = true // TODO
}
