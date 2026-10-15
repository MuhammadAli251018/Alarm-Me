package dev.alidroid.alarmme.preferences

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

@JvmInline
value class PreferencesKey(val value: String)

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface PreferencesValue<T> : SharedFlow<T> {
    val key: PreferencesKey

    /** What this preference reads as when nothing is stored for [key]. */
    val defaultValue: T

    /** The first value that reached this object from storage, or null if [timeout] elapsed first. */
    suspend fun awaitFirstRealValueOrNull(timeout: Duration): T?
}

/** [awaitFirstRealValueOrNull], falling back to [PreferencesValue.defaultValue] on timeout. */
suspend fun <T> PreferencesValue<T>.awaitFirstRealValue(timeout: Duration): T =
    awaitFirstRealValueOrNull(timeout) ?: defaultValue

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
internal class PreferencesValueImpl<T> private constructor(
    private val mutableFlow: MutableSharedFlow<T>,
    override val defaultValue: T,
    override val key: PreferencesKey
) : PreferencesValue<T>, SharedFlow<T> by mutableFlow {

    constructor(defaultValue: T, key: PreferencesKey) :
        this(MutableSharedFlow(replay = 1), defaultValue, key)

    private val storedValueArrived = CompletableDeferred<Unit>()

    internal suspend fun set(value: T) {
        mutableFlow.emit(value)
        storedValueArrived.complete(Unit)
    }

    override suspend fun awaitFirstRealValueOrNull(timeout: Duration): T? =
        withTimeoutOrNull(timeout) {
            storedValueArrived.await()
            mutableFlow.replayCache.lastOrNull() ?: defaultValue
        }
}
