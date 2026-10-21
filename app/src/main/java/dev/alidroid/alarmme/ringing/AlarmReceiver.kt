package dev.alidroid.alarmme.ringing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dev.alidroid.alarmme.alarms.scheduler.alarmIdOrNull
import dev.alidroid.alarmme.alarms.scheduler.putAlarmId

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.alarmIdOrNull() ?: return
        val startAlarmIntent = Intent(context, AlarmRingingService::class.java)
            .putAlarmId(alarmId)

        context?.let { ContextCompat.startForegroundService(it, startAlarmIntent) }
    }
}
