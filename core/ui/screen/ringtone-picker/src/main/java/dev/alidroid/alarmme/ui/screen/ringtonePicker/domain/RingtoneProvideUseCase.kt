package dev.alidroid.alarmme.ui.screen.ringtonePicker.domain

import dev.alidroid.alarmme.alarms.domain.Ringtone
import kotlinx.coroutines.flow.Flow

/**
 * The catalog the picker lists.
 *
 * Deliberately read-only: which ringtone is selected is *this screen's* state until the user
 * saves, at which point it is handed back as a navigation result. Writing the selection through to
 * storage on every tap would commit a choice the user hasn't confirmed.
 */
interface RingtoneProvideUseCase {
    fun getRingtones(): Flow<List<Ringtone>>
}
