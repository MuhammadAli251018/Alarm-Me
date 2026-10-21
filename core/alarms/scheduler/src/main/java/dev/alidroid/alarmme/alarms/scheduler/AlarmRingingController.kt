package dev.alidroid.alarmme.alarms.scheduler

import android.app.Service
import android.content.Context
import android.content.Intent
import dev.alidroid.alarmme.alarms.domain.AlarmId

interface AlarmRingingController {

    /**
     * Asks whoever is ringing [alarmId] to go quiet. Fire-and-forget: the alarm is not necessarily
     * silent by the time this returns, and an alarm that is not ringing is not an error.
     */
    fun stopRinging(alarmId: AlarmId)
}

class AlarmRingingControllerImpl(
    private val context: Context,
    private val serviceClazz: Class<out Service>,
) : AlarmRingingController {

    override fun stopRinging(alarmId: AlarmId) {
        val stopIntent = Intent(context, serviceClazz)
            .setAction(AlarmBroadcast.ACTION_STOP_ALARM)
            .putAlarmId(alarmId)

        runCatching { context.startService(stopIntent) }
    }
}
