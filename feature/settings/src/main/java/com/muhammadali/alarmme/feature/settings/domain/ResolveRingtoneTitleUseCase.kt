package com.muhammadali.alarmme.feature.settings.domain

import com.muhammadali.alarmme.appSettings.RingtonePreferences

interface ResolveRingtoneTitleUseCase {
    suspend fun resolveTitle(ringtone: RingtonePreferences): String
}
