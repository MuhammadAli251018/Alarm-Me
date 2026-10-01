package com.muhammadali.alarmme.feature.settings.data

import com.muhammadali.alarmme.appSettings.RingtonePreferences
import com.muhammadali.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase

/** Mocked: real resolution needs Android's RingtoneManager/Context, not available in this layer yet. */
class ResolveRingtoneTitleUseCaseImpl : ResolveRingtoneTitleUseCase {

    override suspend fun resolveTitle(ringtone: RingtonePreferences): String = when (ringtone) {
        RingtonePreferences.Default -> "Default"
        is RingtonePreferences.Ringtone -> "Custom ringtone"
    }
}
