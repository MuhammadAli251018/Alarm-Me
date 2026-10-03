package dev.alidroid.alarmme

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.scheduler.alarmIdOrNull
import dev.alidroid.alarmme.alarms.scheduler.putAlarmId
import dev.alidroid.alarmme.navigation.ProvideNavGraph
import dev.alidroid.alarmme.navigation.alarmRingingDestination
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider

/** Hosts the ringing screen for one alarm, and is reachable over the lockscreen. */
class RingingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWhenLockedAndTurnScreenOn()
        enableEdgeToEdge()
        // AlarmMeScaffold paints every inset itself, so the system's contrast scrim is not needed.
        // Left on, it tints the 3-button navigation bar a cooler shade than the background behind
        // it — a visible band down the side of the window in landscape.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        val alarmId = intent.alarmIdOrNull()
        if (alarmId == null) {
            // Nothing to ring about — better no window than an empty one over the lockscreen.
            finish()
            return
        }

        setContent {
            AlarmMeThemeProvider {
                ProvideNavGraph(AppNavigationGraph) {
                    AlarmMeScaffold {
                        Navigator(screen = AppNavigationGraph.getScreen(alarmRingingDestination(alarmId)))
                    }
                }
            }
        }
    }

    /** An alarm has to be readable without unlocking, and has to wake a sleeping screen. */
    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    companion object {
        fun intent(context: Context, alarmId: AlarmId): Intent =
            Intent(context, RingingActivity::class.java)
                .putAlarmId(alarmId)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
