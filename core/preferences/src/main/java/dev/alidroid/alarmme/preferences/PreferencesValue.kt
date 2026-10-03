package dev.alidroid.alarmme.preferences

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@JvmInline
value class PreferencesKey(val value: String)

interface PreferencesValue<T> : StateFlow<T> {
    val key: PreferencesKey
}

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
internal class PreferencesValueImpl <T> private constructor(
    private val internalState: MutableStateFlow<T>,
    override val key: PreferencesKey
) : PreferencesValue<T>, StateFlow<T> by internalState {

    constructor(initialValue: T, key: PreferencesKey) : this(MutableStateFlow(initialValue), key)

    internal fun set(value: T) {
        internalState.update { value }
    }
}

fun <T> PreferencesValue(initialValue: T, key: PreferencesKey): PreferencesValue<T> =
    PreferencesValueImpl(initialValue, key)
