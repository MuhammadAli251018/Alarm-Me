package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.widget.Toast
import com.muhammadali.alarmme.common.AlarmConstants
import kotlin.time.Duration.Companion.minutes

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return

        val wakeLock: PowerManager.WakeLock =
            (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmNotificatorImp.WAKE_LOCK_TAG)
            }

        Toast.makeText(context, "Bla Bla", Toast.LENGTH_LONG).show()


        //wake lock ensures that the service starts even when the screen in off
        wakeLock.acquire(2.minutes.inWholeMilliseconds)

        val i = Intent(context, AlarmService::class.java)
        i.action = AlarmNotificatorImp.RECEIVE_ALARM_ACTION
        i.putExtra("alarmId", intent.getIntExtra("alarmId", -1))

        context.startService(i)
    }
}