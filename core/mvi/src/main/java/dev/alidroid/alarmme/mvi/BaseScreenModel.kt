package dev.alidroid.alarmme.mvi

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

private const val MAX_EFFECTS_IN_BUFFER = 16

abstract class BaseScreenModel <State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ScreenModel {

    protected val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    // Note for self (should be moved to docs). Effects should be lightweight to handle and not blocking.
    // For heavy operations it should be launched in different jobs.
    protected val _effect: MutableSharedFlow<Effect> = MutableSharedFlow(
        extraBufferCapacity = MAX_EFFECTS_IN_BUFFER,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val effect: Flow<Effect> = _effect.asSharedFlow()

    abstract fun handleEvent(event: Event)

    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }

    protected inline fun <reified T : State> updateStateIfInstanceOf(noinline reducer: T.() -> State) {
        val capturedState = _state.value
        if (capturedState  is T) {
            _state.value = reducer(capturedState)
        }
    }

    protected fun sendEffect(effect: Effect) {
        _effect.tryEmit(effect)
    }
}