package dev.alidroid.alarmme

import android.app.Application
import dev.alidroid.alarmme.di.appDataModule
import dev.alidroid.alarmme.feature.alarmDetails.di.alarmDetailsModule
import dev.alidroid.alarmme.feature.alarmList.di.alarmListModule
import dev.alidroid.alarmme.feature.alarmRinging.di.alarmRingingModule
import dev.alidroid.alarmme.feature.debugPanel.di.debugPanelModule
import dev.alidroid.alarmme.feature.settings.di.settingsModule
import dev.alidroid.alarmme.ui.screen.ringtonePicker.di.ringtonePickerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AlarmMeApplication : Application() {

    companion object {
        const val IS_DEV_FLAVOR = BuildConfig.FLAVOR == "dev"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlarmMeApplication)

            modules(
                appDataModule,
                alarmListModule,
                ringtonePickerModule,
                alarmDetailsModule,
                settingsModule,
                debugPanelModule,
                alarmRingingModule
            )
        }
    }
}
