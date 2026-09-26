package com.muhammadali.alarmme.ui.screen.ringtonePicker.data

import com.muhammadali.alarmme.alarms.domain.Ringtone
import com.muhammadali.alarmme.alarms.domain.RingtoneRepository
import com.muhammadali.alarmme.ui.screen.ringtonePicker.domain.RingtoneProvideUseCase
import kotlinx.coroutines.flow.Flow

class RingtoneProvideUseCaseImpl(
    private val ringtoneRepository: RingtoneRepository,
) : RingtoneProvideUseCase {

    override fun getRingtones(): Flow<List<Ringtone>> = ringtoneRepository.observeRingtones()
}
