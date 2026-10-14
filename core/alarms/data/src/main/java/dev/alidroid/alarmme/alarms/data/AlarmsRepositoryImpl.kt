package dev.alidroid.alarmme.alarms.data

import android.content.Context
import dev.alidroid.alarmme.alarms.data.db.AlarmDao
import dev.alidroid.alarmme.alarms.data.db.provideAlarmDatabase
import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmsRepository
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun provideAlarmsRepository(context: Context, ringtoneRepository: RingtoneRepository): AlarmsRepository =
    AlarmsRepositoryImpl(provideAlarmDatabase(context).alarmDao(), ringtoneRepository)

class AlarmsRepositoryImpl(
    private val dao: AlarmDao,
    private val ringtoneRepository: RingtoneRepository,
) : AlarmsRepository {

    override fun observeAllAlarms(): Flow<List<Alarm>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain(ringtoneRepository) } }

    override fun observeAlarm(id: AlarmId): Flow<Alarm?> =
        dao.observe(id.value).map { entity -> entity?.toDomain(ringtoneRepository) }

    override suspend fun insertOrUpdate(alarm: Alarm) {
        dao.insertOrUpdate(alarm.toEntity())
    }

    override suspend fun setEnabled(ids: Set<AlarmId>, enabled: Boolean) {
        dao.setEnabled(ids.map { it.value }, enabled)
    }

    override suspend fun delete(ids: Set<AlarmId>) {
        dao.delete(ids.map { it.value })
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
