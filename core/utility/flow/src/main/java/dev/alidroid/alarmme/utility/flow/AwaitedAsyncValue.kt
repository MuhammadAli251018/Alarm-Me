package dev.alidroid.alarmme.utility.flow

import kotlin.time.Duration

sealed interface AwaitedAsyncValue <out T> {
    data class Success<out T>(val value: T): AwaitedAsyncValue<T>
    data class Timeout(val duration: Duration): AwaitedAsyncValue<Nothing>
    data class Failure(val throwable: Throwable): AwaitedAsyncValue<Nothing>
}

fun <T> AwaitedAsyncValue<T>.onSuccess(block: (value: T) -> Unit): AwaitedAsyncValue<T> {
    if (this is AwaitedAsyncValue.Success) block(this.value)
    return this
}

fun <T> AwaitedAsyncValue<T>.onFailure(block: (throwable: Throwable) -> Unit): AwaitedAsyncValue<T> {
    if (this is AwaitedAsyncValue.Failure) block(this.throwable)
    return this
}

fun <T> AwaitedAsyncValue<T>.onTimeout(block: (duration: Duration) -> Unit): AwaitedAsyncValue<T> {
    if (this is AwaitedAsyncValue.Timeout) block(this.duration)
    return this
}

fun <T> AwaitedAsyncValue<T>.onFailureOrTimeout(block: (throwable: Throwable?) -> Unit): AwaitedAsyncValue<T> {
    when (this) {
        is AwaitedAsyncValue.Failure -> block(this.throwable)
        is AwaitedAsyncValue.Timeout -> block(null)
        is AwaitedAsyncValue.Success -> Unit
    }
    return this
}

fun <T> AwaitedAsyncValue<T>.onCompletion(block: () -> Unit): AwaitedAsyncValue<T> {
    block()
    return this
}

internal fun <T> failure(throwable: Throwable): AwaitedAsyncValue<T> =
    AwaitedAsyncValue.Failure(throwable)

internal fun <T> success(value: T): AwaitedAsyncValue<T> =
    AwaitedAsyncValue.Success(value)

internal fun <T> timeout(duration: Duration): AwaitedAsyncValue<T> =
    AwaitedAsyncValue.Timeout(duration)
