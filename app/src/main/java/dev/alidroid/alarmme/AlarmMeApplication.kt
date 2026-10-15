package dev.alidroid.alarmme

import android.app.Application
import dev.alidroid.alarmme.di.appPreferencesModule
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
                appPreferencesModule
            )
        }
    }
}
