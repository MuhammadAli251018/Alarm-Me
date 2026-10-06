package dev.alidroid.alarmme.feature.alarmDetails.presentation

import dev.alidroid.alarmme.alarms.domain.Alarm
import dev.alidroid.alarmme.alarms.domain.AlarmId
import dev.alidroid.alarmme.alarms.domain.AlarmSchedule
import dev.alidroid.alarmme.alarms.domain.Ringtone
import dev.alidroid.alarmme.alarms.domain.RingtoneId
import dev.alidroid.alarmme.feature.alarmDetails.domain.AlarmDetailsUseCase
import dev.alidroid.alarmme.feature.alarmDetails.presentation.fake.FakeAlarmDetailsUseCase
import dev.alidroid.alarmme.feature.alarmDetails.presentation.fake.testAlarm
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEffect
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsEvent
import dev.alidroid.alarmme.feature.alarmDetails.presentation.model.AlarmDetailsState
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

private fun AlarmDetailsScreenModel.collectEffects(): MutableList<AlarmDetailsEffect> {
    val effects = mutableListOf<AlarmDetailsEffect>()
    CoroutineScope(Dispatchers.Main).launch { effect.collect { effects += it } }
    return effects
}

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmDetailsScreenModelTest : StringSpec({

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    "New start mode loads the draft and settles canSave for a schedulable alarm" {
        val draft = testAlarm(schedule = AlarmSchedule.Repeat(DayOfWeek.MONDAY))
        val useCase = FakeAlarmDetailsUseCase(newDraft = draft)

        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)

        val loaded = model.state.value as AlarmDetailsState.Loaded
        loaded.details.label shouldBe draft.label
        loaded.details.time shouldBe draft.time
        loaded.details.repeatDays shouldBe setOf(DayOfWeek.MONDAY)
        loaded.canSave shouldBe true
    }

    "Edit start mode loads the existing alarm's fields" {
        val alarm = testAlarm(
            label = "Gym",
            time = LocalTime(5, 30),
            schedule = AlarmSchedule.Repeat(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        )
        val useCase = FakeAlarmDetailsUseCase(initialAlarm = alarm)

        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.Edit(alarm.id), useCase)

        val loaded = model.state.value as AlarmDetailsState.Loaded
        loaded.details.id shouldBe alarm.id
        loaded.details.label shouldBe alarm.label
        loaded.details.time shouldBe alarm.time
        loaded.details.repeatDays shouldBe setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
    }

    "shows Error when the alarm can't be found, then recovers after OnRetry" {
        val alarm = testAlarm()
        val useCase = FakeAlarmDetailsUseCase(initialAlarm = null)
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.Edit(alarm.id), useCase)

        model.state.value shouldBe AlarmDetailsState.Error

        useCase.alarmFlow.value = alarm
        model.handleEvent(AlarmDetailsEvent.OnRetry)

        (model.state.value as AlarmDetailsState.Loaded).details.id shouldBe alarm.id
    }

    "Event OnDayToggle adds a day new day to the set, then removes it" {
        val draft = testAlarm(schedule = AlarmSchedule.Repeat(DayOfWeek.MONDAY))
        val useCase = FakeAlarmDetailsUseCase(newDraft = draft)
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)

        model.handleEvent(AlarmDetailsEvent.OnDayToggle(DayOfWeek.FRIDAY))
        (model.state.value as AlarmDetailsState.Loaded).details.repeatDays shouldBe
            setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)

        model.handleEvent(AlarmDetailsEvent.OnDayToggle(DayOfWeek.MONDAY))
        (model.state.value as AlarmDetailsState.Loaded).details.repeatDays shouldBe
            setOf(DayOfWeek.FRIDAY)
    }

    "simple field-reducer events update their own field and mark the draft saveable" {
        val useCase = FakeAlarmDetailsUseCase(newDraft = testAlarm())
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)
        val ringtone = Ringtone(RingtoneId("alt"), "Alt", "uri2", null)
        val time = LocalTime(8, 15)

        model.handleEvent(AlarmDetailsEvent.OnCustomDateToggle(true))
        model.handleEvent(AlarmDetailsEvent.OnGradualVolumeToggle(true))
        model.handleEvent(AlarmDetailsEvent.OnVibrateToggle(true))
        model.handleEvent(AlarmDetailsEvent.OnAlarmTimeChanged(time))
        model.handleEvent(AlarmDetailsEvent.OnRingtoneUpdated(ringtone))

        val loaded = model.state.value as AlarmDetailsState.Loaded
        withClue("customDateEnabled") { loaded.details.customDateEnabled shouldBe true }
        withClue("gradualVolumeEnabled") { loaded.details.gradualVolumeEnabled shouldBe true }
        withClue("vibrateEnabled") { loaded.details.vibrateEnabled shouldBe true }
        withClue("time") { loaded.details.time shouldBe time }
        withClue("ringtone") { loaded.details.ringtone shouldBe ringtone }
    }

    "field events are ignored while the state isn't Loaded" {
        val useCase = FakeAlarmDetailsUseCase(initialAlarm = null)
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.Edit(AlarmId.random()), useCase)
        model.state.value shouldBe AlarmDetailsState.Error

        model.handleEvent(AlarmDetailsEvent.OnDayToggle(DayOfWeek.MONDAY))

        model.state.value shouldBe AlarmDetailsState.Error
    }

    "canSave tracks the use case's changed verdict when the schedule is non-empty" {
        val draft = testAlarm(schedule = AlarmSchedule.Repeat(DayOfWeek.MONDAY))
        val useCase = FakeAlarmDetailsUseCase(newDraft = draft)
        useCase.canSaveAlarmFlow.value = false
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)

        (model.state.value as AlarmDetailsState.Loaded).canSave shouldBe false

        useCase.canSaveAlarmFlow.value = true

        (model.state.value as AlarmDetailsState.Loaded).canSave shouldBe true
    }

    "canSave stays false when the schedule is empty even though the draft changed" {
        val draft = testAlarm(schedule = AlarmSchedule.Repeat(DayOfWeek.MONDAY))
        val useCase = FakeAlarmDetailsUseCase(newDraft = draft)
        useCase.canSaveAlarmFlow.value = true
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)

        model.handleEvent(AlarmDetailsEvent.OnDayToggle(DayOfWeek.MONDAY))

        (model.state.value as AlarmDetailsState.Loaded).canSave shouldBe false
    }

    "OnSaveClick saves the mapped alarm and emits NavigateBack when loaded" {
        val draft = testAlarm(label = "Loaded alarm", schedule = AlarmSchedule.Repeat(DayOfWeek.MONDAY))
        val useCase = FakeAlarmDetailsUseCase(newDraft = draft)
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)
        val effects = model.collectEffects()

        model.handleEvent(AlarmDetailsEvent.OnSaveClick)

        useCase.savedAlarm shouldBe draft
        effects shouldBe listOf(AlarmDetailsEffect.NavigateBack)
    }

    "OnSaveClick emits NavigateBack without saving while still Loading" {
        val stuckUseCase = object : AlarmDetailsUseCase by FakeAlarmDetailsUseCase() {
            override suspend fun newAlarmDraft(): Alarm = awaitCancellation()
        }
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, stuckUseCase)
        model.state.value shouldBe AlarmDetailsState.Loading
        val effects = model.collectEffects()

        model.handleEvent(AlarmDetailsEvent.OnSaveClick)

        effects shouldBe listOf(AlarmDetailsEffect.NavigateBack)
        model.state.value shouldBe AlarmDetailsState.Loading
    }

    "OnDeleteClick deletes the alarm and emits NavigateBack in Edit mode" {
        val alarm = testAlarm()
        val useCase = FakeAlarmDetailsUseCase(initialAlarm = alarm)
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.Edit(alarm.id), useCase)
        val effects = model.collectEffects()

        model.handleEvent(AlarmDetailsEvent.OnDeleteClick)

        useCase.deletedAlarmId shouldBe alarm.id
        effects shouldBe listOf(AlarmDetailsEffect.NavigateBack)
    }

    "OnDeleteClick is a no-op in New mode" {
        val useCase = FakeAlarmDetailsUseCase(newDraft = testAlarm())
        val model = AlarmDetailsScreenModel(AlarmDetailsScreen.StartMode.New, useCase)
        val effects = model.collectEffects()

        model.handleEvent(AlarmDetailsEvent.OnDeleteClick)

        useCase.deletedAlarmId shouldBe null
        effects shouldBe emptyList()
    }

    // TODO for future need cases covering snooze and label changes. But not now.
})
