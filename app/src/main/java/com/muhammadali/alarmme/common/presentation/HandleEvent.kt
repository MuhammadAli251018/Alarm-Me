package com.onemorenerd.core.presentation

interface HandleEvent<Event: UiEvent> {
    fun handleEvent(event: Event)
}