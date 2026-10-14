package dev.alidroid.alarmme.alarms.data

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.alarms.domain.RingtoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RingtoneRepositoryImpl(private val context: Context) : RingtoneRepository {

    override fun observeRingtones(): Flow<List<Ringtone>> = flow {
        emit(queryRingtones())
    }.flowOn(Dispatchers.IO)

    override fun observeRingtone(id: RingtoneId): Flow<Ringtone?> = flow {
        emit(queryRingtones().firstOrNull { it.id == id })
    }.flowOn(Dispatchers.IO)

    override fun getDefaultRingtone(): Ringtone {
        val uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
            ?: queryRingtones().first().let { return it }
        return toRingtone(uri)
    }

    private fun queryRingtones(): List<Ringtone> {
        val manager = ringtoneManager()
        val cursor = manager.cursor
        return buildList {
            while (cursor.moveToNext()) {
                val uri = manager.getRingtoneUri(cursor.position)
                val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                add(toRingtone(uri, title))
            }
        }
    }

    private fun ringtoneManager(): RingtoneManager =
        RingtoneManager(context).apply { setType(RingtoneManager.TYPE_ALARM) }

    private fun toRingtone(uri: Uri, title: String? = null): Ringtone {
        val resolvedTitle = title
            ?: RingtoneManager.getRingtone(context, uri)?.getTitle(context)
            ?: uri.toString()
        return Ringtone(
            id = RingtoneId(uri.toString()),
            name = resolvedTitle,
            uri = uri.toString(),
            duration = null,
        )
    }
}
