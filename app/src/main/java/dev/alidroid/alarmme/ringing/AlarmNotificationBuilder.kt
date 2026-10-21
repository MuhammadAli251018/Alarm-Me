package dev.alidroid.alarmme.ringing

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.alidroid.alarmme.R
import dev.alidroid.alarmme.RingingActivity
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.scheduler.hashCode

private const val ALARM_CHANNEL_ID = "alarm_ringing"
const val ALARM_NOTIFICATION_ID = 1

class AlarmNotificationBuilder(context: Context) {

    private val appContext = context.applicationContext
    private val notificationManager = NotificationManagerCompat.from(appContext)

    init {
        notificationManager.ensureAlarmChannel(appContext)
    }

    fun canPostAlarmNotification(): Boolean {
        if (!notificationManager.areNotificationsEnabled()) return false

        val channel = notificationManager.getNotificationChannelCompat(ALARM_CHANNEL_ID)
        return channel == null || channel.importance != NotificationManagerCompat.IMPORTANCE_NONE
    }

    fun build(alarmId: AlarmId): Notification = appContext.ringingNotification(alarmId)
}

private fun NotificationManagerCompat.ensureAlarmChannel(context: Context) {
    val alarmAudio = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    createNotificationChannel(
        NotificationChannelCompat.Builder(
            ALARM_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.alarm_notification_channel_name))
            .setDescription(context.getString(R.string.alarm_notification_channel_description))
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

    return NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_monochrome)
        .setContentTitle(getString(R.string.alarm_notification_title))
        .setContentText(getString(R.string.alarm_notification_text))
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setOngoing(true)
        .setAutoCancel(false)
        .setContentIntent(openRingingScreen)
        .setFullScreenIntent(openRingingScreen, true)
        .build()
}
