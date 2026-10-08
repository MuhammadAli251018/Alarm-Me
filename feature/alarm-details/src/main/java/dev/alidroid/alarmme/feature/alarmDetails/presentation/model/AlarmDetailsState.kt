package dev.alidroid.alarmme.feature.alarmDetails.presentation.model

import dev.alidroid.alarmme.mvi.UiState
import dev.alidroid.alarmme.ui.screen.snoozePicker.SnoozePickerOptions
import kotlin.time.Duration

sealed interface AlarmDetailsState : UiState {
    companion object {
        val Initial = Loading
    }

    data object Loading : AlarmDetailsState

    data class Loaded(
        val details: AlarmDetailsUiState,
        val canSave: Boolean,
        val lengthOptions: List<Duration> = SnoozePickerOptions.Defaults.snoozeLengthOptions,
        val limitOptions: List<Int?> = SnoozePickerOptions.Defaults.snoozeLimitOptions,
    ) : AlarmDetailsState

    data object Error : AlarmDetailsState
}
