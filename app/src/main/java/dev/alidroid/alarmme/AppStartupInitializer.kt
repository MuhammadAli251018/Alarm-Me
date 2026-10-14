package dev.alidroid.alarmme

import android.content.ComponentCallbacks
import dev.alidroid.alarmme.di.appDataModule
import dev.alidroid.alarmme.di.appModule
import dev.alidroid.alarmme.feature.alarmDetails.di.alarmDetailsModule
import dev.alidroid.alarmme.feature.alarmList.di.alarmListModule
import dev.alidroid.alarmme.feature.alarmRinging.di.alarmRingingModule
import dev.alidroid.alarmme.feature.debugPanel.di.debugPanelModule
import dev.alidroid.alarmme.feature.settings.di.settingsModule
import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository
import dev.alidroid.alarmme.featureFlags.awaitMockAlarms
import dev.alidroid.alarmme.featureFlags.awaitMockRingtones
import dev.alidroid.alarmme.ui.screen.ringtonePicker.di.ringtonePickerModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.android.get
import org.koin.core.context.loadKoinModules

object AppStartupInitializer {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    context(component: ComponentCallbacks)
    suspend fun initialize() {
        val featureFlagsRepository = with(component) { get<FeatureFlagsRepository>() }

        initialize(
            mockAlarms = featureFlagsRepository.awaitMockAlarms(),
            mockRingtones = featureFlagsRepository.awaitMockRingtones()
        )
    }

    private fun initialize(mockAlarms: Boolean, mockRingtones: Boolean) {
        if (_isReady.value) error("AppStartupInitializer is already initialized")

            val modules = listOf(
                appModule,
                appDataModule(mockAlarms, mockRingtones),
                alarmListModule,
                ringtonePickerModule,
                alarmDetailsModule,
                settingsModule,
                debugPanelModule,
                alarmRingingModule
            )

            loadKoinModules(modules)

        _isReady.value = true
    }
}