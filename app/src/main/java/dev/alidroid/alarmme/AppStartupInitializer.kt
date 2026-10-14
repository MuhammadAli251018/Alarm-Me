package dev.alidroid.alarmme

import android.content.ComponentCallbacks
import dev.alidroid.alarmme.di.GlobalCoroutineScopeId
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.context.loadKoinModules
import java.util.concurrent.atomic.AtomicBoolean

object AppStartupInitializer {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val started = AtomicBoolean(false)

    /**
     * Loads the deferred half of the graph, once per process. Every UI entry point calls this —
     * whichever one the process happens to start with wins the latch, and the rest just observe
     * [isReady].
     */
    context(component: ComponentCallbacks)
    fun start() {
        if (!started.compareAndSet(false, true)) return

        val scope = with(component) { get<CoroutineScope>(GlobalCoroutineScopeId) }
        scope.launch { initialize() }
    }

    context(component: ComponentCallbacks)
    private suspend fun initialize() {
        val featureFlagsRepository = with(component) { get<FeatureFlagsRepository>() }

        loadModules(
            mockAlarms = featureFlagsRepository.awaitMockAlarms(),
            mockRingtones = featureFlagsRepository.awaitMockRingtones()
        )
    }

    private fun loadModules(mockAlarms: Boolean, mockRingtones: Boolean) {
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
