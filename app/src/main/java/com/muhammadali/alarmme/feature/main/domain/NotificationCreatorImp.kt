package com.muhammadali.alarmme.feature.main.domain

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.common.AlarmExceptions
import com.muhammadali.alarmme.common.Notifications
import com.muhammadali.alarmme.feature.main.AlarmActivity
import com.muhammadali.alarmme.feature.main.ui.screen.util.Time
import com.muhammadali.alarmme.feature.main.ui.screen.util.toTextFormat
import javax.inject.Inject

class NotificationCreatorImp @Inject constructor(
    private val notificationManager: NotificationManager
) : NotificationCreator {

    private fun areNotificationEnabled(context: Context) : Boolean {

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            val channel =
                notificationManager.getNotificationChannel(Notifications.ALARMS_CHANNEL_ID)
            channel?.importance != NotificationManager.IMPORTANCE_NONE
        }
        else {
            val result = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            result == PackageManager.PERMISSION_GRANTED
        }
    }

    /** throws AlarmException if notification in muted */
    override fun fireAlarm(
        context: Context,
        channelId: String,
        time: Time,
        title: String,
        sound: Uri,
        vibration: LongArray,
        enableSnooze: Boolean
    ) {
        if (!areNotificationEnabled(context))
            throw AlarmExceptions.NotificationPermissionNotGranted

        val endAlarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmActivityIntent = Intent(context, AlarmActivity::class.java)
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)

        alarmActivityIntent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("alarmTime", time.toTextFormat())
            putExtra("alarmTitle", title)
            putExtra("alarmSnooze", enableSnooze)
        }

        endAlarmIntent.action = AlarmConstants.END_ALARM_ACTION
        snoozeIntent.action = AlarmConstants.SNOOZE_ALARM_ACTION

        val alarmPendingIntent = PendingIntent.getActivities(
            context,
            AlarmConstants.START_ALARM_ACTIVITY_REQUEST_CODE,
            arrayOf(alarmActivityIntent),
            PendingIntent.FLAG_IMMUTABLE
        )

        val endPendingIntent = PendingIntent.getBroadcast(context,
            AlarmConstants.END_ALARM_REQUEST_CODE,
            endAlarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val snoozePendingIntent = PendingIntent.getBroadcast(context,
            AlarmConstants.SNOOZE_ALARM_REQUEST_CODE,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context,
            Notifications.ALARMS_CHANNEL_ID)
            .setContentTitle(time.toTextFormat(getOnlyTime = true))
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(R.drawable.ic_launcher_foreground, "End", endPendingIntent)
            .setSound(sound)
            .setVibrate(vibration)
            .setFullScreenIntent(alarmPendingIntent, true)

        if(enableSnooze)
            notification.addAction(R.drawable.ic_launcher_foreground, "End", snoozePendingIntent)

        notificationManager.notify(AlarmConstants.ALARM_NOTIFICATION_ID, notification.build())
    }
}