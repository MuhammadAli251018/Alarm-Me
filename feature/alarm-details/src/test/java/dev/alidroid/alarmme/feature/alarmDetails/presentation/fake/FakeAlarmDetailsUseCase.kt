package dev.alidroid.alarmme.feature.alarmDetails.presentation.fake

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAlarmDetailsUseCase(
    initialAlarm: Alarm? = null,
    var newDraft: Alarm = testAlarm(),
) : AlarmDetailsUseCase {

    val alarmFlow = MutableStateFlow(initialAlarm)
    val canSaveAlarmFlow = MutableStateFlow(true)

    var savedAlarm: Alarm? = null
        private set
    var deletedAlarmId: AlarmId? = null
        private set

    override fun getAlarm(id: AlarmId): Flow<Alarm?> = alarmFlow

    override fun canSaveAlarm(alarm: Alarm): Flow<Boolean> = canSaveAlarmFlow

    override suspend fun newAlarmDraft(): Alarm = newDraft

    override suspend fun saveAlarm(alarm: Alarm) {
        savedAlarm = alarm
    }

    override suspend fun deleteAlarm(id: AlarmId) {
        deletedAlarmId = id
    }
}
