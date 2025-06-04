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
import com.muhammadali.alarmme.feature.main.AlarmActivity
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotification
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmNotificator

//  todo manage the Context to avoid memory leaks

class AlarmNotificatorImp (private val context: Context) : AlarmNotificator {

    companion object {

        // Actions
        const val END_ALARM_ACTION = "end_alarm_action"
        const val SNOOZE_ALARM_ACTION = "snooze_alarm_action"
        const val RECEIVE_ALARM_ACTION = "receive_alarm_action"

        const val ALARMS_CHANNEL_ID = "alarms_channel_id"
        const val ALARMS_CHANNEL_NAME = "Alarms"

        const val WAKE_LOCK_TAG = "Alarm-Me::MyWakelockTag"
        const val alarmIdKey = "alarmId"
        const val END_ALARM_REQUEST_CODE = 3
        const val SNOOZE_ALARM_REQUEST_CODE = 4
        const val START_ALARM_REQUEST_CODE = 5
    }

    //private fun <T> executeWithContext(operation: (context: Context) -> T): T = operation(context)

    private fun <T> executeWithNotificationManager(operation: (NotificationManager) -> T): T {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return operation(notificationManager)
    }

    private fun isNotificationEnabled() : Boolean = executeWithNotificationManager { notificationManager ->

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            val channel =
                notificationManager.getNotificationChannel(ALARMS_CHANNEL_ID)
            channel?.importance != NotificationManager.IMPORTANCE_NONE
        }
        else {
            val result = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            result == PackageManager.PERMISSION_GRANTED
        }
    }
    override fun fireAlarm(alarmNotification: AlarmNotification) = executeWithNotificationManager { notificationManager ->
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
            START_ALARM_REQUEST_CODE,
            arrayOf(alarmActivityIntent),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
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

        val notification = NotificationCompat.Builder(context, ALARMS_CHANNEL_ID)
            .setContentTitle(alarmNotification.title)
            .setContentText(alarmNotification.time)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(R.drawable.ic_launcher_foreground, "End Alarm", endPendingIntent)
            .setSound(ringtone, AudioManager.STREAM_ALARM)
            .setVibrate(alarmNotification.vibrate)
            .setContentIntent(alarmPendingIntent)
            .setFullScreenIntent(alarmPendingIntent, true)
            .setAutoCancel(true)
            .setOngoing(true)
            .setLocalOnly(true)



        if (alarmNotification.allowSnooze)
            notification.addAction(R.drawable.ic_launcher_foreground, "Snooze", snoozePendingIntent)

        notificationManager.notify(
            alarmNotification.id,
            notification.build()
        )
    }

    override fun cancelAlarm(alarmId: Int) = executeWithNotificationManager { notificationManager ->
        notificationManager.cancel(alarmId)
    }
}