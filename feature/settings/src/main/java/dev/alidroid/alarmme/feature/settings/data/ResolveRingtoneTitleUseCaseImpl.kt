package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase

/** Mocked: real resolution needs Android's RingtoneManager/Context, not available in this layer yet. */
class ResolveRingtoneTitleUseCaseImpl : ResolveRingtoneTitleUseCase {

    override suspend fun resolveTitle(ringtone: RingtonePreferences): String = when (ringtone) {
        RingtonePreferences.Default -> "Default"
        is RingtonePreferences.Ringtone -> "Custom ringtone"
    }
}
