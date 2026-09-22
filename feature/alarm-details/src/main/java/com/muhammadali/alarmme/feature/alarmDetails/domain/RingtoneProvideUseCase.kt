package com.muhammadali.alarmme.feature.alarmDetails.domain

data class RingtoneDetails(val id: String, val name: String)

interface RingtoneProvideUseCase {
    suspend fun getRingtonesDetails(id: String): RingtoneDetails?
}