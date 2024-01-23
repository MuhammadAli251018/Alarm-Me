package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.muhammadali.alarmme.common.AlarmConstants

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return

        val wakeLock: PowerManager.WakeLock =
            (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmConstants.WAKE_LOCK_TAG)
            }

        //wake lock ensures that the service starts even when the screen in off
        wakeLock.acquire(2*60*1000L)

        context.startService(intent)
    }
}