package dev.alidroid.alarmme.feature.debugPanel.data

import dev.alidroid.alarmme.featureFlags.FeatureFlagsRepository
import dev.alidroid.alarmme.preferences.PreferencesValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/** Records what the use case writes. The read side is not part of what these cases exercise. */
private class RecordingFeatureFlagsRepository(
    var globalMocks: Boolean = true,
    var mockAlarms: Boolean = true,
    var mockRingtones: Boolean = true,
) : FeatureFlagsRepository {

    override fun observeGlobalMocks(): PreferencesValue<Boolean> = error("reads are not under test")
    override suspend fun setGlobalMocks(enabled: Boolean) { globalMocks = enabled }

    override fun observeMockAlarms(): PreferencesValue<Boolean> = error("reads are not under test")
    override suspend fun setMockAlarms(enabled: Boolean) { mockAlarms = enabled }

    override fun observeMockRingtones(): PreferencesValue<Boolean> = error("reads are not under test")
    override suspend fun setMockRingtones(enabled: Boolean) { mockRingtones = enabled }

    override suspend fun clear() {
        globalMocks = false
        mockAlarms = false
        mockRingtones = false
    }
}

class FeatureFlagsUpdateUseCaseImplTest : StringSpec({

    // The DI wiring reads the individual mock flags, not the master, so leaving a stored `true`
    // behind would have the app using a mock the panel is showing as off.
    "switching global mocks off also clears the flags it governs" {
        val repository = RecordingFeatureFlagsRepository(
            globalMocks = true,
            mockAlarms = true,
            mockRingtones = true
        )

        FeatureFlagsUpdateUseCaseImpl(repository).setGlobalMocks(false)

        repository.globalMocks shouldBe false
        repository.mockAlarms shouldBe false
        repository.mockRingtones shouldBe false
    }

    // Switching the master back on is not a restore — the governed flags stay where they were and
    // are re-picked deliberately.
    "switching global mocks on leaves the flags it governs untouched" {
        val repository = RecordingFeatureFlagsRepository(
            globalMocks = false,
            mockAlarms = false,
            mockRingtones = false
        )

        FeatureFlagsUpdateUseCaseImpl(repository).setGlobalMocks(true)

        repository.globalMocks shouldBe true
        repository.mockAlarms shouldBe false
        repository.mockRingtones shouldBe false
    }

    "the individual flags are still settable on their own" {
        val repository = RecordingFeatureFlagsRepository(
            globalMocks = true,
            mockAlarms = false,
            mockRingtones = false
        )
        val useCase = FeatureFlagsUpdateUseCaseImpl(repository)

        useCase.setMockAlarms(true)

        repository.globalMocks shouldBe true
        repository.mockAlarms shouldBe true
        repository.mockRingtones shouldBe false
    }
})
