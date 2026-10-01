package com.muhammadali.alarmme.feature.debugPanel.data

import com.muhammadali.alarmme.alarms.domain.AlarmsRepository
import com.muhammadali.alarmme.feature.debugPanel.domain.ScheduledAlarmsCountUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduledAlarmsCountUseCaseImpl(
    private val alarmsRepository: AlarmsRepository,
) : ScheduledAlarmsCountUseCase {

    override fun observeScheduledAlarmsCount(): Flow<Int> =
        alarmsRepository.observeAllAlarms().map { alarms -> alarms.count { it.enabled } }
}
