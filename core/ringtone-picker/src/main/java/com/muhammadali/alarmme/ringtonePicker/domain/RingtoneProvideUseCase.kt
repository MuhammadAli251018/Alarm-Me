package com.muhammadali.alarmme.ringtonePicker.domain

import com.muhammadali.alarmme.ringtonePicker.presentatoin.component.RingtoneItem
import kotlinx.coroutines.flow.Flow

interface RingtoneProvideUseCase {
    fun getRingtones(): Flow<List<RingtoneItem>>
    fun getSelectedRingtoneId(): Flow<String?>
    suspend fun selectRingtone(id: String)
}
