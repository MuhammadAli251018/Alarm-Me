package com.muhammadali.alarmme.feature.alarmDetails.data

import com.muhammadali.alarmme.feature.alarmDetails.domain.RingtoneDetails
import com.muhammadali.alarmme.feature.alarmDetails.domain.RingtoneProvideUseCase

private val SAMPLE_RINGTONE_NAMES = listOf(
    "Sunrise Chime",
    "Morning Birds",
    "Gentle Waves",
    "Crystal Bells",
    "Soft Piano",
)

class InMemoryRingtoneProvideUseCase(
    private val ringtoneNames: List<String> = SAMPLE_RINGTONE_NAMES,
) : RingtoneProvideUseCase {
    override suspend fun getRingtonesDetails(id: String): RingtoneDetails =
        RingtoneDetails(id = id, name = ringtoneNames.random())
}