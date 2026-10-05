# Feature Module Conventions

This file is the rulebook for what lives inside a feature's `presentation/` package. The
`component/` vs `model/`, and the `Screen`/`ScreenModel` pair.

```
/presentation
----/component/
----/model/
----/mapper/ (optional can be replaced with Mapper.kt file at the root level of feature)
----/XScreen.kt
----/XScreenModel.kt
```

## model/ vs component/

- **`model/`** contains the MVI models, and the feature's shared component modules.
- **`component/`** holds reusable, self-contained `@Composable` functions, a row, a list, a top
  bar, a skeleton. A composable in here can define its own small, local, Compose-only UI state,
  if shared with other component it's preferred ot place it in `model/`.
- The screen's own top-level content-switch composable stays in the `Screen.kt` file itself,
  next to the `Screen` class — not in `component/`. 
  Only composables that are reusable on their own terms get extracted there.

The rule in one line: **`model/` is data the `ScreenModel` owns; `component/` is rendering, plus
whatever local state only exists to support that rendering.**

## The Mapper

Converting a domain model into a presentation model is the Mapper's job — the domain →
presentation boundary. It's a plain top-level/extension function (not a class), named `toX`
(e.g. `toLoadedState`, `toItemState`), 
and it lives inside `mapper/` or the feature root-level package if one file, right beside the state type
it produces — not in a separate top-level `Mapper.kt`. Typical file name: `XMapper.kt`
(e.g. `AlarmListMapper.kt`).

```kotlin
internal fun List<Alarm>.toLoadedState(now: LocalDateTime): AlarmsListState.Loaded =
    AlarmsListState.Loaded(
        alarms = map { it.toItemState() },
        nextAlarmTime = filter { it.enabled }.mapNotNull { it.nextOccurrence(now) }.minOrNull()
    )

internal fun Alarm.toItemState(): AlarmItemState = AlarmItemState(
    id = id, enabled = enabled, time = time, description = description()
)
```

A couple of things worth naming as convention: mapper functions should be `internal` nothing outside
the feature should ever call them directly. And the mapper's job is assembling the result, not
formatting values from scratch — shared formatting helpers (date/time labels, etc.) live in
`core:utility:*` and get called from here, not reimplemented.

## Screen and ScreenModel

- **`XScreenModel`** is the screen's MVI brain: it extends `BaseScreenModel<XState, XEvent,
  XEffect>` and implements `handleEvent` (*see `02-mvi.md` for what `State`/`Event`/`Effect` and
  `BaseScreenModel` actually are*).

```kotlin
class XScreenModel(
    private val useCase: XUseCase
) : BaseScreenModel<XState, XEvent, XEffect>(initialState = XState.Initial) {
    override fun handleEvent(event: XEvent) { /* ... */ }
}
```

- **`XScreen`** is the Voyager entry point. 
- The Screen is responsible for wiring the `ScreenModel` with the compose UI, talking to the `Navigator`.
  Its `Content()` always does the same handful of things: 
  - resolve the `ScreenModel` via `koinScreenModel()`.
  - collect `state` with `collectAsState()`.
  - alias `screenModel::handleEvent` as `dispatch`.
  then hand all of it to a stateless `XScreenContent` composable. The `Screen` itself holds no logic, every
    callback it builds either calls the `Navigator` directly (forward navigation is a `Screen`-level
    concern) or `dispatch`es an event for the `ScreenModel` to handle.

```kotlin
class XScreen(/*...*/) : Screen {
    @Composable
    override fun Content() {
        val screenModel: XScreenModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val dispatch = screenModel::handleEvent

        XScreenContent(
            state = state,
            onSomeClick = { dispatch(XEvent.OnSomeClick) },
        )
    }
}
```

- **`XScreenContent`** is the stateless composable `XScreen` delegates to — it lives in the same
  file as `XScreen`, takes `state` plus one callback per `XEvent`, and switches on `state` to pick
  which per-state composable to render (loading/loaded/error, etc). This content-switch
  composable, and the small per-state branches it routes to, are the one exception to "composables
  live in `component/`" — see above.
