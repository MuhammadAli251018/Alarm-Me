package dev.alidroid.alarmme.feature.alarmDetails.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.isScheduled
import dev.alidroid.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEffect
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsUiState
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.toAlarm
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.toUiState
import dev.alidroid.alarmme.mvi.BaseScreenModel
import dev.alidroid.alarmme.ui.screen.snoozePicker.SnoozePickerOptions
import dev.alidroid.alarmme.utility.kotlinDateTime.now
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class AlarmDetailsScreenModel(
    private val startMode: AlarmDetailsScreen.StartMode,
    private val alarmDetailsUseCase: AlarmDetailsUseCase,
) : BaseScreenModel<AlarmDetailsState, AlarmDetailsEvent, AlarmDetailsEffect>(
    initialState = AlarmDetailsState.Initial
) {
    var observeAlarmJob: Job? = null
    var updateSaveButtonJob: Job? = null
    private var isAlarmEnabled = false
    private val snoozeLengthOptions = SnoozePickerOptions.Defaults.snoozeLengthOptions
    private val snoozeLimitOptions = SnoozePickerOptions.Defaults.snoozeLimitOptions

    init {
        initiateScreenData()
    }

    override fun handleEvent(event: AlarmDetailsEvent) {
        when (event) {
            is AlarmDetailsEvent.OnDayToggle -> updateLoadedState { draft ->
                draft.copy(
                    repeatDays = if (draft.repeatDays.contains(event.day)) {
                        draft.repeatDays - event.day
                    } else {
                        draft.repeatDays + event.day
                    }
                )
            }

            is AlarmDetailsEvent.OnCustomDateToggle -> updateLoadedState { draft ->
                draft.copy(customDateEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnGradualVolumeToggle -> updateLoadedState { draft ->
                draft.copy(gradualVolumeEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnVibrateToggle -> updateLoadedState { draft ->
                draft.copy(vibrateEnabled = event.enabled)
            }

            is AlarmDetailsEvent.OnCustomDatesChanged -> updateLoadedState { draft ->
                draft.copy(customDates = event.dates.sorted())
            }

            is AlarmDetailsEvent.OnAlarmTimeChanged -> updateLoadedState { draft ->
                draft.copy(time = event.time)
            }

            is AlarmDetailsEvent.OnSnoozeChanged -> updateLoadedState { draft ->
                draft.copy(
                    snoozeLengthIndex = event.lengthIndex,
                    snoozeLimitIndex = event.limitIndex
                )
            }

            // TODO: wire once the label editor exists.
            AlarmDetailsEvent.OnLabelClick -> Unit

            AlarmDetailsEvent.OnSaveClick -> {
                saveDraft()
                sendEffect(AlarmDetailsEffect.NavigateBack)
            }

            AlarmDetailsEvent.OnDeleteClick -> screenModelScope.launch {
                val alarmId = (startMode as? AlarmDetailsScreen.StartMode.Edit)?.alarmId
                    ?: return@launch
                alarmDetailsUseCase.deleteAlarm(alarmId)
                sendEffect(AlarmDetailsEffect.NavigateBack)
            }

            AlarmDetailsEvent.OnRetry -> initiateScreenData()

            is AlarmDetailsEvent.OnRingtoneUpdated -> updateLoadedState { draft ->
                draft.copy(ringtone = event.ringtone)
            }
        }
    }


    private inline fun updateLoadedState(crossinline reducer: (AlarmDetailsUiState) -> AlarmDetailsUiState) {
        updateStateIfInstanceOf<AlarmDetailsState.Loaded> {
            copy(details = reducer(details), canSave = true)
        }
    }

    private fun saveDraft() {
        val loaded = state.value as? AlarmDetailsState.Loaded ?: return
        screenModelScope.launch {
            alarmDetailsUseCase.saveAlarm(
                loaded.details.toAlarm(isAlarmEnabled, loaded.lengthOptions, loaded.limitOptions)
            )
        }
    }

    private suspend fun updateSaveButton(isAlarmEnabled: Boolean) {
        state.filterIsInstance<AlarmDetailsState.Loaded>()
            .collectLatest { newState ->
                val alarm = newState.details.toAlarm(isAlarmEnabled, newState.lengthOptions, newState.limitOptions)

                alarmDetailsUseCase.canSaveAlarm(alarm)
                    .distinctUntilChanged()
                    .collect { isChanged ->
                        val canSave = alarm.schedule.isScheduled && isChanged /*&& newState.details.label.isNotBlank()*/
                        updateStateIfInstanceOf<AlarmDetailsState.Loaded> {
                            copy(canSave = canSave)
                        }
                    }
            }
    }

    fun initiateScreenData() {
        observeAlarmJob?.cancel()
        updateSaveButtonJob?.cancel()

        observeAlarmJob = screenModelScope.launch {
            val alarm = getAlarm()
            if (alarm == null) {
                updateState { AlarmDetailsState.Error }
                return@launch
            }

            val now = LocalDateTime.now()
            val uiState = alarm.toUiState(now, snoozeLengthOptions, snoozeLimitOptions)
            updateState {
                AlarmDetailsState.Loaded(
                    uiState,
                    canSave = false,
                    lengthOptions = snoozeLengthOptions,
                    limitOptions = snoozeLimitOptions
                )
            }
            isAlarmEnabled = alarm.enabled

            updateSaveButtonJob = launch { updateSaveButton(isAlarmEnabled) }
        }
    }

    suspend fun getAlarm(): Alarm?  {
        return when (startMode) {
            is AlarmDetailsScreen.StartMode.Edit -> alarmDetailsUseCase.getAlarm(startMode.alarmId).first()
            AlarmDetailsScreen.StartMode.New -> alarmDetailsUseCase.newAlarmDraft()
        }
    }
}
