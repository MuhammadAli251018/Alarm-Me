package com.muhammadali.alarmme.alarms.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

interface RingtoneRepository {

    fun observeRingtones(): Flow<List<Ringtone>>

    fun observeRingtone(id: RingtoneId): Flow<Ringtone?>

    fun getDefaultRingtone(): Ringtone
}

suspend fun RingtoneRepository.getRingtone(id: RingtoneId): Ringtone? =
    observeRingtone(id).firstOrNull()