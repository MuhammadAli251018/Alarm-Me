# MVI

Every screen is built as MVI: a single `State`, a closed set of `Event`s a user can fire, and
optional `Effect`s for one-off reactions. The contract isn't just convention — it's enforced by a
shared base class in `core:mvi`, which has no Compose dependency, so none of this requires Compose
to use or to test.

The three roles are bare marker interfaces:

```kotlin
interface UiState
interface UiEvent
interface UiEffect
```

`BaseScreenModel` is where the actual behavior lives — every `XScreenModel` extends it:

```kotlin
abstract class BaseScreenModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ScreenModel {

    abstract fun handleEvent(event: Event)

    protected fun updateState(reducer: State.() -> State) { /*...*/ }
    protected fun sendEffect(effect: Effect) { /*...*/ }
}
```
- Effects should stay lightweight and non-blocking — if an event needs to do real work before
  firing one, launch that work in its own `screenModelScope` job rather than blocking inside
  `sendEffect`.
- `handleEvent` is the single entry point for every user action; a `ScreenModel` has no other
  public mutation surface.

**A feature example.** `feature:alarm-details` shows the shape in practice. `AlarmDetailsState` is
a sealed interface of `Loading` / `Loaded(details, canSave)` / `Error`; `AlarmDetailsEvent` is a
sealed interface of `On*`-prefixed events (`OnAlarmTimeChanged`, `OnSaveClick`, `OnDeleteClick`,
`OnRetry`, ...). This `XState` / `XEvent` naming is the same pattern used across every feature.

It's also the one place in the codebase that currently wires up a real `Effect`:

```kotlin
sealed interface AlarmDetailsEffect : UiEffect {
    data object NavigateBack : AlarmDetailsEffect
}
```

The `ScreenModel` fires it after a successful save, and also when the alarm being viewed turns
out to have been deleted elsewhere while the screen was open:

```kotlin
sendEffect(AlarmDetailsEffect.NavigateBack)
```

and the `Screen` collects it once, as a reaction, not as something it stores:

```kotlin
LaunchedEffect(screenModel) {
    screenModel.effect.collect { effect ->
        when (effect) {
            AlarmDetailsEffect.NavigateBack -> navigator?.pop()
        }
    }
}
```

Screens have no shared base class of their own — every feature hand-rolls the same few lines in
`Content()`: grab the `ScreenModel` via `koinScreenModel()`, `collectAsState()` the state,
alias `screenModel::handleEvent` as `dispatch`, grab `LocalNavigator.current`, and — when the
feature has effects worth reacting to — collect them through a `LaunchedEffect` like the one
above.

**Why effects, not state fields, for one-off events.** One-time UI reactions — a dialog that
should pop up once, a snackbar, "navigate back now" — are modeled as `Effect`s fired through
`sendEffect`, not as a nullable/boolean field living in `State`. 
The reason is far easier to test those effects with unit tests. 
The project leans on unit tests + previews rather than end-to-end Compose
UI tests (*see `00-philosophy.md`*), and `core:mvi`'s `State`/`Effect` split exists specifically to
keep that possible without Compose in the loop.

An `Effect` is fire-and-forget: a test calls `handleEvent(...)` and asserts the right value showed
up on `screenModel.effect` — done, nothing left to clean up. A one-shot flag living in `State`
instead needs to be manually reset/consumed by whoever reads it, which turns every test touching
that state into "assert it fired, then simulate consuming it, then assert it's gone" — three
partitions to cover instead of one, for behavior that was never really part of the screen's
*state* to begin with.

The codebase isn't fully consistent with this yet — a couple of spots still route one-off
UI behavior outside this convention — but those are known gaps to be refactored later, not a
second pattern to follow.

# Navigation

Voyager is the navigation library: every screen is a `Screen`, pushed and popped on a `Navigator`.

`core:navigation` wraps this with a small indirection layer, containing screen ids and parameters,
so features don't depend on each other to navigate between screens:

- `NavDestination` is an opaque, parcelable target (a `ScreenId` plus optional parameters).
- `NavigationGraph` is a single-method interface — `getScreen(destination): Screen` — provided
  once at the composition root via `LocalNavGraph`, and is the only place that actually knows how
  a `NavDestination` maps to a concrete `Screen` class.
- `Navigator.navigateTo(destination, navigationGraph)` use it to perform the navigation, 
it's an extension function that just wraps `push(navigationGraph.getScreen(destination))`.

This lets a feature push to a screen it has no compile-time dependency on, as long as it has a
`NavDestination` for it.

**Passing results back.** When a pushed screen needs to hand data back to whoever opened it (the
ringtone picker handing back the tone the user picked), that goes through
`NavResultRepository`/`AwaitNavResult` rather than through `BaseScreenModel`'s own `State`/`Event`.
The picker screen saves its result under a typed key when the user confirms:

See example in `feature:alarm-details` & `feature:ringtone-picker`:
```kotlin
navResultRepository.save(RINGTONE_NAV_RESULT_KEY, PickedRingtoneResult(selectedRingtone))
navigator?.pop()
```

and the caller awaits it declaratively:

```kotlin
AwaitNavResult(RingtonePickerScreen.RINGTONE_NAV_RESULT_KEY) { result ->
    dispatch(AlarmDetailsEvent.OnRingtoneUpdated(result.ringtone))
}
```

Note that the result itself still flows back in through a normal `Event` (`OnRingtoneUpdated`) —
`NavResultRepository` only solves getting the value across the screen boundary; the `ScreenModel`
still owns what happens with it.

**Where this meets MVI.** Forward navigation (opening another screen) is a direct `Navigator`
call from the `Screen`. Backward navigation driven by business logic — "this alarm no longer
exists, leave this screen" — is different: it isn't a UI click, it's a decision the `ScreenModel`
makes, so it belongs behind an `Effect` instead, exactly as `AlarmDetailsEffect.NavigateBack`
does above.
