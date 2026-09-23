package com.muhammadali.alarmme.feature.alarmDetails.domain

import com.muhammadali.alarmme.alarms.domain.Ringtone
import com.muhammadali.alarmme.alarms.domain.RingtoneId

/** Resolves the id the ringtone picker hands back into the tone to store on the alarm. */
interface RingtoneProvideUseCase {
    suspend fun getRingtone(id: RingtoneId): Ringtone?
    suspend fun getDefaultRingtone(): Ringtone
}