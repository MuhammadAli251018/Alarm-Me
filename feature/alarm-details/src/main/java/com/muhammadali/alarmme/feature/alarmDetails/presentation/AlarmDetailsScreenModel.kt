package com.muhammadali.alarmme.feature.alarmDetails.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.muhammadali.alarmme.alarms.domain.Alarm
import com.muhammadali.alarmme.alarms.domain.AlarmId
import com.muhammadali.alarmme.alarms.domain.nextOccurrence
import com.muhammadali.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEffect
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsUiState
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.toAlarm
import com.muhammadali.alarmme.feature.alarmDetails.presentation.model.toUiState
import com.muhammadali.alarmme.mvi.BaseScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import com.muhammadali.alarmme.utility.kotlinDateTime.now
import kotlinx.datetime.LocalDateTime

class AlarmDetailsScreenModel(
    private val startMode: AlarmDetailsScreen.StartMode,
    private val alarmDetailsUseCase: AlarmDetailsUseCase,
) : BaseScreenModel<AlarmDetailsState, AlarmDetailsEvent, AlarmDetailsEffect>(
    initialState = AlarmDetailsState.Initial
) {

    private var alarmLoadingJob: Job? = null

    /** The original alarm that was loaded from the data source. */
    private var originalAlarm: Alarm? = null

    // TODO: NEEDS CAREFUL EXAMINATION IN FUTURE, PROBABLY THERE IS A BETTER WAY TO DO THIS
    /** Whether the observed alarm has arrived, so a later null reads as a deletion, not a bad id. */
    private var hasLoaded = false

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

            // TODO: wire once the snooze / label editors exist.
            AlarmDetailsEvent.OnSnoozeClick,
            AlarmDetailsEvent.OnLabelClick -> Unit

            AlarmDetailsEvent.OnSaveClick -> saveDraft()

            AlarmDetailsEvent.OnDeleteClick -> screenModelScope.launch {
                val alarmId = (startMode as? AlarmDetailsScreen.StartMode.Edit)?.alarmId
                    ?: return@launch
                alarmDetailsUseCase.deleteAlarm(alarmId)
            }

            AlarmDetailsEvent.OnRetry -> loadAlarmDetails()

            is AlarmDetailsEvent.OnRingtoneUpdated -> updateDraft { draft ->
                draft.copy(ringtone = event.ringtone)
            }
        }
    }

    private fun saveDraft() {
        val loaded = state.value as? AlarmDetailsState.Loaded ?: return
        val base = originalAlarm ?: return

        screenModelScope.launch {
            val alarm = loaded.details.toAlarm(base)

            alarmDetailsUseCase.saveAlarm(alarm)

            // The saved alarm is the new baseline, so Save goes quiet until something changes again.
            originalAlarm = alarm
            updateStateIfInstanceOf<AlarmDetailsState.Loaded> { copy(canSave = false) }
        }
    }

    private inline fun updateDraft(crossinline reducer: (AlarmDetailsUiState) -> AlarmDetailsUiState) {
        updateStateIfInstanceOf<AlarmDetailsState.Loaded> {
            val draft = reducer(details).withRecomputedOccurrence()
            copy(
                details = draft,
                // Compared field-by-field against the baseline, so undoing an edit by hand
                // correctly quiets Save again.
                canSave = draft != originalAlarm?.toUiState(LocalDateTime.now())
            )
        }
    }

    /** "RINGS IN …" has to follow edits to the time or the schedule, both of which move it. */
    private fun AlarmDetailsUiState.withRecomputedOccurrence(): AlarmDetailsUiState {
        val base = originalAlarm ?: return this
        val candidate = toAlarm(base)
        return copy(nextOccurrence = candidate.nextOccurrence(LocalDateTime.now()))
    }

    private fun loadAlarmDetails() {
        alarmLoadingJob?.cancel()
        _state.value = AlarmDetailsState.Loading
        hasLoaded = false

        alarmLoadingJob = screenModelScope.launch {
            when (startMode) {
                AlarmDetailsScreen.StartMode.New -> loadNewAlarm()
                is AlarmDetailsScreen.StartMode.Edit -> loadExistingAlarm(startMode.alarmId)
            }
        }
    }

    private suspend fun loadNewAlarm() {
        // A draft is built, not observed: nothing is stored until Save, so there is no source to
        // follow and no reason for this to be a flow.
        val draft = runCatching { alarmDetailsUseCase.newAlarmDraft() }
            .getOrElse {
                _state.value = AlarmDetailsState.Error
                return
            }

        originalAlarm = draft
        hasLoaded = true
        _state.value = AlarmDetailsState.Loaded(details = draft.toUiState(LocalDateTime.now()), canSave = false)
    }

    private suspend fun loadExistingAlarm(alarmId: AlarmId) {
        alarmDetailsUseCase.getAlarm(alarmId)
            .catch { _state.value = AlarmDetailsState.Error }
            .collect { alarm ->
                when {
                    alarm != null -> {
                        originalAlarm = alarm
                        hasLoaded = true
                        // Re-project only while the draft is clean, so a store update never
                        // overwrites edits the user has in flight.
                        val isDirty = (state.value as? AlarmDetailsState.Loaded)?.canSave == true
                        if (!isDirty) {
                            _state.value = AlarmDetailsState.Loaded(
                                details = alarm.toUiState(LocalDateTime.now()),
                                canSave = false
                            )
                        }
                    }

                    // Gone after we had it: the user deleted it. Leave, don't cry failure.
                    hasLoaded -> sendEffect(AlarmDetailsEffect.NavigateBack)

                    // Never there to begin with — a genuinely bad id.
                    else -> _state.value = AlarmDetailsState.Error
                }
            }
    }
}
