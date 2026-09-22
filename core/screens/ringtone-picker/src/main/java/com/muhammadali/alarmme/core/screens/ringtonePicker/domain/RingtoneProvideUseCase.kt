package com.muhammadali.alarmme.core.screens.ringtonePicker.domain

import com.muhammadali.alarmme.core.screens.ringtonePicker.presentatoin.component.RingtoneItem
import kotlinx.coroutines.flow.Flow

interface RingtoneProvideUseCase {
    fun getRingtones(): Flow<List<RingtoneItem>>
    fun getSelectedRingtoneId(): Flow<String?>
    suspend fun selectRingtone(id: String)
}
