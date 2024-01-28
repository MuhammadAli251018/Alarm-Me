package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.muhammadali.alarmme.R
import com.muhammadali.alarmme.common.AlarmConstants
import com.muhammadali.alarmme.common.Notifications
import com.muhammadali.alarmme.feature.main.AlarmActivity
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotificator

class AlarmNotificatorImp (private val contextProvider: ContextProvider) : AlarmNotificator {

    companion object {
        const val ALARMS_CHANNEL_ID = "alarms_channel_id"
        const val ALARMS_CHANNEL_NAME = "Alarms"
        const val END_ALARM_ACTION = "end_alarm_action"
        const val SNOOZE_ALARM_ACTION = "snooze_alarm_action"
        const val RECEIVE_ALARM_ACTION = "receive_alarm_action"
        const val WAKE_LOCK_TAG = "Alarm-Me::MyWakelockTag"
        const val alarmIdKey = "alarmId"
        const val alarmTitleKey = "alarmTitle"
        const val alarmTimeKey = "alarmTime"
        const val alarmSoundUriKey = "alarmSoundUri"
        const val alarmVibrationKey = "alarmVibration"
        const val alarmSnoozeKey = "alarmSnooze"
        const val alarmNotificationId = "alarm_notification_id"

        const val ALARM_PENDING_INTENT_ID = 0
        const val END_ALARM_REQUEST_CODE = 3
        const val SNOOZE_ALARM_REQUEST_CODE = 4
        const val START_ALARM_ACTIVITY_REQUEST_CODE = 5
        const val ALARM_NOTIFICATION_ID = 6
    }

    private fun <T> executeWithContext(operation: (context: Context) -> T): T = operation(contextProvider.getContext())

    private fun <T> executeWithNotificationManager(operation: (NotificationManager, Context) -> T): T = executeWithContext {context ->
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        operation(notificationManager, context)
    }

    private fun isNotificationEnabled() : Boolean = executeWithNotificationManager { notificationManager, context ->

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            val channel =
                notificationManager.getNotificationChannel(Notifications.ALARMS_CHANNEL_ID)
            channel?.importance != NotificationManager.IMPORTANCE_NONE
        }
        else {
            val result = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            result == PackageManager.PERMISSION_GRANTED
        }
    }
    override fun fireAlarm(alarmNotification: AlarmNotification) = executeWithNotificationManager { notificationManager, context ->
        // check if notification allowed
        if (!isNotificationEnabled()) {
            Toast.makeText(context, "Notification not enabled", Toast.LENGTH_LONG).show()

            throw Exception("Notification not enabled"/*todo*/)
        }

        val ringtone = Uri.parse(alarmNotification.ringtoneRef)
        val endAlarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmActivityIntent = Intent(context, AlarmActivity::class.java)
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)


        endAlarmIntent.action = END_ALARM_ACTION
        snoozeIntent.action = SNOOZE_ALARM_ACTION

        val alarmPendingIntent = PendingIntent.getActivities(
            context,
            START_ALARM_ACTIVITY_REQUEST_CODE,
            arrayOf(alarmActivityIntent),
            PendingIntent.FLAG_IMMUTABLE
        )

        val endPendingIntent = PendingIntent.getBroadcast(context,
            END_ALARM_REQUEST_CODE,
            endAlarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val snoozePendingIntent = PendingIntent.getBroadcast(context,
            SNOOZE_ALARM_REQUEST_CODE,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        //val vibration

        val notification = NotificationCompat.Builder(context, ALARMS_CHANNEL_ID)
            .setContentTitle(alarmNotification.title)
            .setContentText(alarmNotification.time)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(R.drawable.ic_launcher_foreground, "End", endPendingIntent)
            .setSound(ringtone, AudioManager.STREAM_ALARM)
            .setVibrate(alarmNotification.vibrate)
            .setContentIntent(alarmPendingIntent)
            .setFullScreenIntent(alarmPendingIntent, true)


        if (alarmNotification.allowSnooze)
            notification.addAction(R.drawable.ic_launcher_foreground, "Snooze", snoozePendingIntent)

        notificationManager.notify(
            alarmNotification.id,
            notification.build()
        )
    }

    override fun cancelAlarm(alarmId: Int) = executeWithNotificationManager { notificationManager, context ->
        notificationManager.cancel(alarmId)
    }
}