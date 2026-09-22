package com.muhammadali.alarmme.feature.alarmDetails.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetails
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsId
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import com.muhammadali.alarmme.feature.alarmDetails.domain.RingtoneProvideUseCase
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import com.muhammadali.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AlarmDetailsScreenModel(
    private val startMode: AlarmDetailsScreen.StartMode,
    private val alarmDetailsUseCase: AlarmDetailsUseCase,
    private val ringtoneProvideUseCase: RingtoneProvideUseCase
) : BaseScreenModel<AlarmDetailsState, AlarmDetailsEvent, Nothing>(
    initialState = AlarmDetailsState.Initial
) {

    private var alarmLoadingJob: Job? = null
    private var original: AlarmDetails? = null

    init {
        loadAlarmDetails()
    }

    override fun handleEvent(event: AlarmDetailsEvent) {
        when (event) {
            is AlarmDetailsEvent.OnDayToggle -> updateDraft { draft ->
                draft.copy(
                    repeatDays = if (draft.repeatDays.contains(event.day)) {
                        draft.repeatDays - event.day
                    } else {
                        draft.repeatDays + event.day
                    }
                )
            }

            is AlarmDetailsEvent.OnCustomDateToggle -> updateDraft { draft ->
                draft.copy(customDateEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnGradualVolumeToggle -> updateDraft { draft ->
                draft.copy(gradualVolumeEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnVibrateToggle -> updateDraft { draft ->
                draft.copy(vibrateEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnCustomDatesChanged -> updateDraft { draft ->
                draft.copy(customDates = event.dates.sorted())
            }

            is AlarmDetailsEvent.OnAlarmTimeChanged -> updateDraft { draft ->
                draft.copy(time = event.time)
            }

            // TODO: wire once the ringtone picker / snooze / label flows exist.
            AlarmDetailsEvent.OnSnoozeClick,
            AlarmDetailsEvent.OnLabelClick -> Unit

            AlarmDetailsEvent.OnSaveClick -> {
                val loaded = state.value as? AlarmDetailsState.Loaded ?: return
                screenModelScope.launch {
                    alarmDetailsUseCase.saveAlarmDetails(loaded.alarmDetails)
                    original = loaded.alarmDetails

                    updateStateIfInstanceOf<AlarmDetailsState.Loaded> {
                        copy(canSave = false)
                    }
                }
            }

            AlarmDetailsEvent.OnDeleteClick -> screenModelScope.launch {
                val alarmId = (startMode as? AlarmDetailsScreen.StartMode.Edit)?.alarmId ?: return@launch
                alarmDetailsUseCase.deleteAlarmDetails(alarmId)
            }

            AlarmDetailsEvent.OnRetry -> loadAlarmDetails()

            is AlarmDetailsEvent.OnRingtoneUpdated -> {
                screenModelScope.launch {
                    val ringtoneDetails =
                        ringtoneProvideUseCase.getRingtonesDetails(event.pickerRingtone.ringtoneId)
                            ?: return@launch

                    updateDraft { draft -> draft.copy(soundName = ringtoneDetails.name) }
                }
            }
        }
    }

    private inline fun updateDraft(crossinline reducer: (AlarmDetails) -> AlarmDetails) {
        updateStateIfInstanceOf<AlarmDetailsState.Loaded> {
            val draft = reducer(alarmDetails)
            copy(alarmDetails = draft, canSave = draft != original)
        }
    }

    private fun loadAlarmDetails() {
        alarmLoadingJob?.cancel()
        _state.value = AlarmDetailsState.Loading

        alarmLoadingJob = screenModelScope.launch {
            when (startMode) {
                AlarmDetailsScreen.StartMode.New -> loadNewAlarm()
                is AlarmDetailsScreen.StartMode.Edit -> loadExistingAlarm(startMode.alarmId)
            }
        }
    }

    private suspend fun loadNewAlarm() {
        alarmDetailsUseCase.draftNewAlarm()
            .catch { _state.value = AlarmDetailsState.Error }
            .onStart { delay(2.seconds) } // To give loading animation enough time.
            .collect { details ->
                original = details
                _state.value = AlarmDetailsState.Loaded(alarmDetails = details, canSave = false)
            }
    }

    private suspend fun loadExistingAlarm(alarmId: AlarmDetailsId) {
        alarmDetailsUseCase.getAlarmDetails(alarmId)
            .catch { _state.value = AlarmDetailsState.Error }
            .collect { result ->

                val details = result
                    .onFailure { showErrorScreen() }
                    .getOrNull() ?: return@collect

                original = details
                _state.value = AlarmDetailsState.Loaded(alarmDetails = details, canSave = false)
            }
    }

    private fun showErrorScreen() {
        _state.value = AlarmDetailsState.Error
    }
}
