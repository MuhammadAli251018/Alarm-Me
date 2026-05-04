package com.muhammadali.alarmme.feature.main.presentaion.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import android.widget.Toast

private const val TAG = "AlarmReceiver"
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return

        (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmNotificatorImp.WAKE_LOCK_TAG).apply {
                acquire(10*60*1000L)
            }
        }

        //Toast.makeText(context, "Bla Bla", Toast.LENGTH_LONG).show()


        //wake lock ensures that the service starts even when the screen in off

        Log.d(TAG, "received Alarm with Id: ${intent.getStringExtra(AlarmSchedulerImp.AlARM_ID_KEY)}")
        val i = Intent(context, AlarmService::class.java)
        i.action = AlarmNotificatorImp.RECEIVE_ALARM_ACTION
        i.putExtra(AlarmSchedulerImp.AlARM_ID_KEY, intent.getStringExtra(AlarmSchedulerImp.AlARM_ID_KEY))

        context.startService(i)
    }
}