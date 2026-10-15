package dev.alidroid.alarmme.feature.alarmRinging.domain

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import kotlinx.coroutines.flow.Flow

interface AlarmRingingUseCase {
    /** Emits null when nothing is stored under [id] — a stale or unknown alarm. */
    fun observeRingingAlarm(id: AlarmId): Flow<Alarm?>

    /**
     * Arms the occurrence after the one that just rang. An alarm with nothing left on its
     * schedule — a one-shot, or custom dates that have all passed — is left unarmed.
     */
    suspend fun dismissRingingAlarm(id: AlarmId)
}
