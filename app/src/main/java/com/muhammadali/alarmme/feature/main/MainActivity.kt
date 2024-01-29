package com.muhammadali.alarmme.feature.main

import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.muhammadali.alarmme.common.ui.theme.AlarmMeTheme
import com.muhammadali.alarmme.common.util.TimeAdapterImp
import com.muhammadali.alarmme.feature.main.data.local.AlarmsDB
import com.muhammadali.alarmme.feature.main.data.repo.AlarmsDbRepoImp
import com.muhammadali.alarmme.feature.main.domain.entities.Alarm
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmPreferences
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmReceiver
import com.muhammadali.alarmme.feature.main.presentaion.alarmservice.AlarmSchedulerImp
import com.muhammadali.alarmme.feature.main.presentaion.screen.navigation.MainActivityNavHost
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel.AlarmDataScreenVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private fun getContext() = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val dataScreenVM by viewModels<AlarmDataScreenVM>()
        val scheduler = AlarmSchedulerImp(
            AlarmReceiver::class.java, this)

        CoroutineScope(Dispatchers.Default).launch {
            val alarm = Alarm(
                alarmId = 1,
                title = "test",
                time = System.currentTimeMillis() + (5.seconds.inWholeMilliseconds),
                preferences = AlarmPreferences(
                    AlarmPreferences.Snooze.NoSnooze,
                    vibration = true,
                    ringtoneRef = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        .toString(),
                    repeat = AlarmPreferences.RepeatPattern.Weekly(listOf(AlarmPreferences.RepeatPattern.Weekly.DaysOfWeeks.Monday).toSet())
                ),
                enabled = true
            )

            scheduler.scheduleOrUpdate(
                alarm = alarm
            )

            //Toast.makeText(this@MainActivity, "scheduled", Toast.LENGTH_LONG).show()
        }
        setContent {
            AlarmMeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    /*MainActivityNavHost(
                        context = this,
                        dataScreenVM = dataScreenVM,
                        navController = navController)
                }*/
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlarmMeTheme {
        Greeting("Android")
    }

}