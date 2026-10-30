package dev.alidroid.alarmme.utility.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
abstract class AsyncResult <T> : AbstractFlow<AwaitedAsyncValue<T>>()

internal class AsyncResultImpl <T> (
    private val operationFlow: Flow<T?>,
    private val timeoutDuration: Duration
) : AsyncResult<T>() {

    override suspend fun collectSafely(collector: FlowCollector<AwaitedAsyncValue<T>>) {
        val result: AwaitedAsyncValue<T> = try {
            val value = withTimeoutOrNull(timeoutDuration) {
                operationFlow.filterNotNull().first()
            }
            if (value != null) success(value) else timeout(timeoutDuration)
        } catch (e: CancellationException) {
            throw e
        } catch (throwable: Throwable) {
            failure(throwable)
        }
        collector.emit(result)
    }
}

/** ANR takes 5 seconds to fire, so it was used as default duration.*/

val DefaultTimeout = 5.seconds

fun <T> Flow<T?>.asAsyncResult(timeoutDuration: Duration = DefaultTimeout): AsyncResult<T> =
    AsyncResultImpl(this, timeoutDuration)

fun <T> asyncResult(timeoutDuration: Duration, operation: suspend () -> T?): AsyncResult<T> =
    flow { emit(operation()) }.asAsyncResult(timeoutDuration)
