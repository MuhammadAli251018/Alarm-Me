package dev.alidroid.alarmme.ringing

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal typealias AlarmMeRingtone = dev.alidroid.alarmme.alarms.domain.Ringtone

class AlarmRingtonePlayer(
    context: Context,
    private val ringtone: AlarmMeRingtone
) {
    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null

    fun startRinging() {
        if (mediaPlayer != null) return

        mediaPlayer = ringtoneCandidates().firstNotNullOfOrNull { uri -> play(uri) }
    }

    suspend fun stopRinging() {
        val player = mediaPlayer ?: return
        mediaPlayer = null

        withContext(Dispatchers.IO) {
            runCatching { player.stop() }
            player.release()
        }
    }

    private fun ringtoneCandidates(): List<Uri> = listOfNotNull(
        ringtone.uri.takeIf { it.isNotBlank() }?.toUri(),
        RingtoneManager.getActualDefaultRingtoneUri(appContext, RingtoneManager.TYPE_ALARM),
        Settings.System.DEFAULT_ALARM_ALERT_URI
    ).distinct()

    private fun play(uri: Uri): MediaPlayer? {
        val player = MediaPlayer()

        return runCatching {
            player.apply {
                setAudioAttributes(ALARM_PLAYBACK_ATTRIBUTES)
                isLooping = true
                setWakeMode(appContext, PowerManager.PARTIAL_WAKE_LOCK)
                setDataSource(appContext, uri)
                prepare()
                start()
            }
        }.getOrElse {
            player.release()
            null
        }
    }
}

/** Alarm usage puts the sound on the alarm stream, and past Do Not Disturb. */
private val ALARM_PLAYBACK_ATTRIBUTES = AudioAttributes.Builder()
    .setUsage(AudioAttributes.USAGE_ALARM)
    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
    .build()
