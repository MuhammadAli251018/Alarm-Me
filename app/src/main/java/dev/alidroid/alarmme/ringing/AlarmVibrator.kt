package dev.alidroid.alarmme.ringing

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

private val ALARM_VIBRATION_TIMINGS = longArrayOf(0, 1000, 500)

private const val ALARM_VIBRATION_REPEAT_INDEX = 1

class AlarmVibrator(context: Context) {

    private val vibrator: Vibrator? = context.alarmVibratorOrNull()

    @Suppress("DEPRECATION")
    fun startVibration() {
        val vibrator = vibrator ?: return

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> vibrator.vibrate(
                alarmWaveform(),
                VibrationAttributes.createForUsage(VibrationAttributes.USAGE_ALARM)
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                vibrator.vibrate(alarmWaveform(), ALARM_AUDIO_ATTRIBUTES)

            else -> vibrator.vibrate(
                ALARM_VIBRATION_TIMINGS,
                ALARM_VIBRATION_REPEAT_INDEX,
                ALARM_AUDIO_ATTRIBUTES
            )
        }
    }

    fun stopVibration() {
        vibrator?.cancel()
    }
}

/** Vibration predates [VibrationAttributes], and below API 33 it is described in audio terms. */
private val ALARM_AUDIO_ATTRIBUTES = AudioAttributes.Builder()
    .setUsage(AudioAttributes.USAGE_ALARM)
    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
    .build()

@RequiresApi(Build.VERSION_CODES.O)
private fun alarmWaveform(): VibrationEffect =
    VibrationEffect.createWaveform(ALARM_VIBRATION_TIMINGS, ALARM_VIBRATION_REPEAT_INDEX)

private fun Context.alarmVibratorOrNull(): Vibrator? {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Vibrator::class.java)
    }

    return vibrator?.takeIf { it.hasVibrator() }
}
