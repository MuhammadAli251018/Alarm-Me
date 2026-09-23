package com.muhammadali.alarmme.feature.alarmDetails.data

import com.muhammadali.alarmme.alarms.domain.Ringtone
import com.muhammadali.alarmme.alarms.domain.RingtoneId
import com.muhammadali.alarmme.alarms.domain.RingtoneRepository
import com.muhammadali.alarmme.alarms.domain.getRingtone
import com.muhammadali.alarmme.feature.alarmDetails.domain.RingtoneProvideUseCase

class RingtoneProvideUseCaseImpl(
    private val ringtoneRepository: RingtoneRepository,
) : RingtoneProvideUseCase {

    override suspend fun getRingtone(id: RingtoneId): Ringtone? =
        ringtoneRepository.getRingtone(id)

    override suspend fun getDefaultRingtone(): Ringtone {
        return ringtoneRepository.getDefaultRingtone()
    }
}
