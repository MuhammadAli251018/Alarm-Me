package dev.alidroid.alarmme

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.scheduler.alarmIdOrNull
import dev.alidroid.alarmme.alarms.scheduler.hashCode

private const val ALARM_CHANNEL_ID = "alarm_ringing"
private const val ALARM_CHANNEL_NAME = "Ringing alarms"
private const val ALARM_CHANNEL_DESCRIPTION = "Full-screen alerts for alarms that are going off."
private const val ALARM_NOTIFICATION_TITLE = "Alarm"
private const val ALARM_NOTIFICATION_TEXT = "Your alarm is ringing."

/**
 * Where a scheduled alarm lands. A receiver can't reliably start an activity from the
 * background, so it posts a full-screen-intent notification instead and lets the system bring
 * [RingingActivity] up — full screen when the device is locked, a heads-up notification when
 * it is in use.
 */
class AlarmReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        val appContext = context?.applicationContext ?: return
        // Without an id there is no way to know which alarm went off, so there is nothing to show.
        val alarmId = intent?.alarmIdOrNull() ?: return

        val notificationManager = NotificationManagerCompat.from(appContext)
        notificationManager.ensureAlarmChannel()
        notificationManager.notify(alarmId.hashCode, appContext.ringingNotification(alarmId))
    }
}

/** Creating a channel that already exists only updates its name and description. */
private fun NotificationManagerCompat.ensureAlarmChannel() {
    val alarmAudio = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    createNotificationChannel(
        NotificationChannelCompat.Builder(
            ALARM_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(ALARM_CHANNEL_NAME)
            .setDescription(ALARM_CHANNEL_DESCRIPTION)
            // Playback lands later; for now the ringing screen does the alerting, not the channel.
            .setSound(null, alarmAudio)
            .build()
    )
}

private fun Context.ringingNotification(alarmId: AlarmId): Notification {
    val openRingingScreen = PendingIntent.getActivity(
        this,
        alarmId.hashCode,
        RingingActivity.intent(this, alarmId),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // No alarm is read here: a receiver has seconds to run, and the screen loads the alarm itself.
    return NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
        // The design system has no alarm glyph yet, so the monochrome launcher mark stands in.
        .setSmallIcon(R.drawable.ic_launcher_monochrome)
        .setContentTitle(ALARM_NOTIFICATION_TITLE)
        .setContentText(ALARM_NOTIFICATION_TEXT)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setOngoing(true)
        .setAutoCancel(false)
        .setContentIntent(openRingingScreen)
        .setFullScreenIntent(openRingingScreen, true)
        .build()
}
