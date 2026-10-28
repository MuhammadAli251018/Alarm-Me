package dev.alidroid.alarmme.utility.flow

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

private val AwaitTimeout = 1.seconds

class AsyncResultTest : StringSpec({

    "a value arriving within the timeout is a Success" {
        runTest {
            flowOf<Int?>(42).asAsyncResult(AwaitTimeout).first() shouldBe
                AwaitedAsyncValue.Success(42)
        }
    }

    "nulls ahead of the value are skipped" {
        runTest {
            flowOf(null, 7).asAsyncResult(AwaitTimeout).first() shouldBe
                AwaitedAsyncValue.Success(7)
        }
    }

    "a flow that stays open without emitting times out" {
        runTest {
            flow<Int?> { awaitCancellation() }.asAsyncResult(AwaitTimeout).first() shouldBe
                AwaitedAsyncValue.Timeout(AwaitTimeout)
        }
    }

    "a throwing operation is a Failure carrying the cause" {
        runTest {
            val exception = IllegalStateException()

            flow<Int?> { throw exception }.asAsyncResult(AwaitTimeout).first() shouldBe
                AwaitedAsyncValue.Failure(exception)
        }
    }

    "a flow that completes without emitting is a Failure, not a Timeout" {
        runTest {
            val result = emptyFlow<Int?>().asAsyncResult(AwaitTimeout).first()

            val failure = result.shouldBeInstanceOf<AwaitedAsyncValue.Failure>()
            failure.throwable.shouldBeInstanceOf<NoSuchElementException>()
        }
    }

    "asyncResult wraps a suspending operation" {
        runTest {
            asyncResult(AwaitTimeout) { 5 }
                .first() shouldBe AwaitedAsyncValue.Success(5)
        }
    }

    "asyncResult returning null is a Failure" {
        runTest {
            val result = asyncResult<Int>(AwaitTimeout) { null }.first()

            val failure = result.shouldBeInstanceOf<AwaitedAsyncValue.Failure>()
            failure.throwable.shouldBeInstanceOf<NoSuchElementException>()
        }
    }

    "cancellation propagates instead of being reported as a Failure" {
        runTest {
            shouldThrow<CancellationException> {
                flow<Int?> { throw CancellationException("cancelled") }
                    .asAsyncResult(AwaitTimeout)
                    .first()
            }
        }
    }
})
