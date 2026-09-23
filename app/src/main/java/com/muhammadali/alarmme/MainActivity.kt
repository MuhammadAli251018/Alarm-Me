package com.muhammadali.alarmme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsId
import com.muhammadali.alarmme.feature.alarmDetails.presentation.AlarmDetailsScreen
import com.muhammadali.alarmme.feature.alarmList.presentation.AlarmListScreen
import com.muhammadali.alarmme.ringtonePicker.presentatoin.RingtonePickerScreen
import com.muhammadali.alarmme.navigation.ProvideNavResultRepository
import com.muhammadali.alarmme.navigation.screenDestination
import com.muhammadali.alarmme.ui.theme.AlarmMeThemeProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmMeThemeProvider {
                ProvideNavResultRepository {
                    Navigator(
                        AlarmListScreen(
                            alarmDetailsDestination = { alarmId ->
                                screenDestination {
                                    AlarmDetailsScreen(
                                        startMode = if (alarmId != null) {
                                            AlarmDetailsScreen.StartMode.Edit(AlarmDetailsId(alarmId.value))
                                        } else {
                                            AlarmDetailsScreen.StartMode.New
                                        },
                                        ringtonePickerDestination = screenDestination { RingtonePickerScreen() }
                                    )
                                }
                            }
                        )
                    )
                }
            }
        }
    }
}
