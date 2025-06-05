package com.muhammadali.alarmme.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onemorenerd.core.presentation.HandleEvent
import com.onemorenerd.core.presentation.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : Any, Event : UiEvent, Effect : UiEffect>(
    initialState: State,
) : ViewModel(), HandleEvent<Event> {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    private val _effect = MutableSharedFlow<Effect>(replay = 1)
    val effect = _effect.asSharedFlow()

    protected fun updateState(update: State.() -> State) {
        _state.update(update)
    }

    protected fun pushEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}

@Composable
fun <Event : UiEvent> BaseViewModel<*, Event, *>.rememberDispatch(): (Event) -> Unit {
    return remember { { event: Event -> this.handleEvent(event) } }
}