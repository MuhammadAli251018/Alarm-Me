package dev.alidroid.alarmme.preferences

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.seconds

private val SomeKey = PreferencesKey("a_preference")

class PreferencesValueTest : StringSpec({

    // The regression that left the Settings screen loading forever: a screen opens long after
    // startup, subscribes, and nothing writes the preference again — so without replay it waits
    // for an emission that never comes, and the combine() it feeds never produces a first element.
    "a collector subscribing after a value is stored still receives it" {
        runTest {
            val preference = PreferencesValueImpl(defaultValue = 0, key = SomeKey)

            preference.set(7)

            preference.first() shouldBe 7
        }
    }

    // Why this is a SharedFlow and not a StateFlow: storing the value a preference already holds
    // has to reach collectors, and StateFlow is defined to conflate equal values away.
    "equal consecutive values are both delivered" {
        runTest {
            val preference = PreferencesValueImpl(defaultValue = 0, key = SomeKey)
            val received = async(start = CoroutineStart.UNDISPATCHED) {
                preference.take(2).toList()
            }

            preference.set(5)
            preference.set(5)

            received.await() shouldBe listOf(5, 5)
        }
    }

    "awaitFirstRealValueOrNull returns a value stored before the wait began" {
        runTest {
            val preference = PreferencesValueImpl(defaultValue = 0, key = SomeKey)

            preference.set(42)

            preference.awaitFirstRealValueOrNull(1.seconds) shouldBe 42
        }
    }

    // The other half of the race. The latch has to hold whichever side goes first, so neither
    // ordering can drop the notification and leave the caller waiting out the whole timeout.
    "awaitFirstRealValueOrNull returns a value stored after the wait began" {
        runTest {
            val preference = PreferencesValueImpl(defaultValue = 0, key = SomeKey)
            val awaited = async(start = CoroutineStart.UNDISPATCHED) {
                preference.awaitFirstRealValueOrNull(1.seconds)
            }

            preference.set(9)

            awaited.await() shouldBe 9
        }
    }

    "awaitFirstRealValueOrNull gives up when storage never answers" {
        runTest {
            val preference = PreferencesValueImpl(defaultValue = 0, key = SomeKey)

            preference.awaitFirstRealValueOrNull(1.seconds) shouldBe null
        }
    }

    "awaitFirstRealValue falls back to the declared default when storage never answers" {
        runTest {
            val preference = PreferencesValueImpl(defaultValue = 3, key = SomeKey)

            preference.awaitFirstRealValue(1.seconds) shouldBe 3
        }
    }
})
