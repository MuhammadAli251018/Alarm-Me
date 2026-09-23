package com.muhammadali.alarmme.ringtonePicker.data

import com.muhammadali.alarmme.ringtonePicker.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.ringtonePicker.presentatoin.component.RingtoneItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/** Long enough for the loading skeleton to be seen, short enough not to be in the way. */
private val INITIAL_LOAD_DELAY: Duration = 600.milliseconds

/**
 * A [RingtoneProvideUseCase] that keeps everything in memory.
 *
 * It exists so the ringtone picker can be driven by hand — previewing and selecting all work —
 * while the real data layer (system ringtones / custom sounds) is still missing. Nothing
 * survives process death, and it is deliberately the only place that knows that: swapping it for
 * the real implementation in `ringtonePickerModule` is the whole migration.
 */
class InMemoryRingtoneProvideUseCase(
    initialRingtones: List<RingtoneItem> = sampleRingtones(),
    initialSelectedRingtoneId: String? = initialRingtones.firstOrNull()?.id,
    private val initialLoadDelay: Duration = INITIAL_LOAD_DELAY,
) : RingtoneProvideUseCase {

    private val ringtones = MutableStateFlow(initialRingtones)
    private val selectedRingtoneId = MutableStateFlow(initialSelectedRingtoneId)

    override fun getRingtones(): Flow<List<RingtoneItem>> = flow {
        delay(initialLoadDelay)
        emitAll(ringtones)
    }

    override fun getSelectedRingtoneId(): Flow<String?> = selectedRingtoneId.asStateFlow()

    override suspend fun selectRingtone(id: String) {
        selectedRingtoneId.value = id
    }
}

internal fun sampleRingtones(): List<RingtoneItem> = listOf(
    RingtoneItem("sunrise-chime", "Sunrise chime", "Gentle", 42.seconds),
    RingtoneItem("slow-bell", "Slow bell", "Gentle", 64.seconds),
    RingtoneItem("morning-birds", "Morning birds", "Natural", 130.seconds),
    RingtoneItem("ocean-drift", "Ocean drift", "Ambient", 90.seconds),
    RingtoneItem("woodland-whistle", "Woodland whistle", "Natural", 55.seconds),
)
