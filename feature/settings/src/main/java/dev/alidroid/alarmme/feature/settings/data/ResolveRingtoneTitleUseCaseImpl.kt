package dev.alidroid.alarmme.feature.settings.data

import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import dev.alidroid.alarmme.alarms.domain.getRingtone
import dev.alidroid.alarmme.appSettings.RingtonePreferences
import dev.alidroid.alarmme.feature.settings.domain.ResolveRingtoneTitleUseCase

class ResolveRingtoneTitleUseCaseImpl(
    private val ringtoneRepository: RingtoneRepository,
) : ResolveRingtoneTitleUseCase {

    override suspend fun resolveTitle(ringtone: RingtonePreferences): String = when (ringtone) {
        RingtonePreferences.Default -> ringtoneRepository.getDefaultRingtone().name
        is RingtonePreferences.Ringtone -> ringtoneRepository.getRingtone(RingtoneId(ringtone.id))?.name
            ?: ringtoneRepository.getDefaultRingtone().name
    }
}
