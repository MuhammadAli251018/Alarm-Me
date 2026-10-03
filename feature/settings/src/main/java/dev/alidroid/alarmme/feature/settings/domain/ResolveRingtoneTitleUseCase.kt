package dev.alidroid.alarmme.feature.settings.domain

import dev.alidroid.alarmme.appSettings.RingtonePreferences

interface ResolveRingtoneTitleUseCase {
    suspend fun resolveTitle(ringtone: RingtonePreferences): String
}
