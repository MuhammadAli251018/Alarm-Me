# Project Structure

An overview of the rules behind Alarm Me's structure.

## 1. Philosophy

- **Multi-module Gradle** — split into independent modules instead of one `app`
  module, so dependency direction is enforced by Gradle itself, not convention.
- **Feature modularization** — each feature (`feature:alarm-list`, …) is a
  self-contained vertical slice. Features never depend on each other.
- **Clean-ish layering** — inside a feature, `domain` (contracts) never depends on
  `data` (impl) or `presentation` (UI). Lightweight, not ceremony-heavy.
- **MVI** — every screen has one `State`, one set of `Event`s, optional `Effect`s,
  enforced by a shared base class rather than convention alone.
- **Reusable components** — screens are composed from small composables; shared ones
  live in `core:ui`, feature-specific ones live in the feature.

## 2. Project Structure

```
# settings.gradle.kts
include(":app")
include(":core:ui")
include(":core:mvi")
include(":core:utility:compose")
include(":core:ringtone-picker")
include(":feature:alarm-list")
// ...
include(":feature:xxx")
```

Dependency direction: `feature:* → core:*`, `app → feature:* + core:*`. Never the
reverse, and never `feature:* → feature:*`.

- **`core:*`** — foundations only, no feature knowledge, never depends on `feature:*`.
  - `core:mvi` — `UiState`/`UiEvent`/`UiEffect` + `BaseScreenModel`. Pure Kotlin, no
    Compose dependency.
  - `core:ui` — the design system: `theme/` (custom `AlarmMeColorScheme`, read via
    `LocalAlarmMeColors.current`, not `MaterialTheme`), `component/` (`Am*`-prefixed
    atoms), `screen/` (shared full-screen states like `ErrorScreenContent`),
    `screenCategory/` (responsive layout — see §3). Expected to keep growing as
    features surface repeated patterns.
  - `core:utility:compose` — small, dependency-free Compose extensions (e.g.
    `Modifier.modifyIf`). Sibling `core:utility:<concern>` modules should be added if a
    non-Compose utility need shows up, rather than growing this one.
  - `core:ringtone-picker` — a shared, fully-built screen (same internal shape as a
    feature: `domain/ → data/ → presentation/` + `di/`) that more than one `feature:*`
    module needs to navigate to or depend on directly.
- **`feature:*`** — depends on whichever `core:*` modules it needs (typically `core:ui`,
  `core:mvi`, `core:utility:compose`). Each is split into `domain/ → data/ → presentation/`, plus
  `di/` exposing one Koin module registered in `AlarmMeApplication`. `feature:alarm-list`
  is the reference layout; `feature:alarm-details` is a stub still to be built out the
  same way.
- **`app`** — composition root only: starts Koin, hosts the root Voyager `Navigator`.
  The one module allowed to depend on more than one feature.

## 3. MVI & Components

Every screen follows the same shape (`core:mvi`'s `BaseScreenModel`):

```kotlin
sealed interface XState : UiState { data object Loading; data class Loaded(...); data object Error }
sealed interface XEvent : UiEvent { data object OnRetry; data class OnXClick(...) }

class XScreenModel(...) : BaseScreenModel<XState, XEvent, Nothing>(XState.Initial) {
    override fun handleEvent(event: XEvent) = when (event) { ... }
}
```

`XScreen.Content()` collects `state`, passes `screenModel::handleEvent` down, and hands
off to a stateless `XScreenContent(state, on*Click, ...)` — that's the previewable part.

Break the loaded-state UI into small composables under `presentation/component/` (one
piece of visual identity per file); promote a component to `core:ui/component/` once a
second feature needs it. **Every reusable composable needs previews covering all its
states** (loading/loaded/error, selected/unselected, etc.).

### `ScreenCategory`

When a component's *layout* (not just content) needs to differ by window size, read it
in the composable:

```kotlin
val screenCategory = currentScreenCategory()
Modifier.screenConstraints(screenCategory) // core:utility:compose
```

For previews, default to `@CommonScreenCategoriesPreview` (the handful of buckets real
devices land in, light + dark). Full annotation list and an exhaustive
`@AllScreenCategoriesPreview` live in
`core/ui/src/main/java/.../ui/screenCategory/PreviewAnnotations.kt` — read that file
directly rather than this doc for the complete set.

## 4. Naming Conventions

| Thing | Convention | Example |
|---|---|---|
| Gradle module | `core:<name>` / `feature:<kebab-case>` | `core:ringtone-picker` |
| Package | `com.muhammadali.alarmme.<segment>`, camelCase | `ringtonePicker` |
| Screen | `XScreen.kt` (Voyager `Screen`) | `RingtonePickerScreen` |
| Screen model | `XScreenModel.kt` | `RingtonePickerScreenModel` |
| State / Event | `XState.kt` / `XEvent.kt`, events prefixed `On*` | `RingtonePickerState` |
| DI module | `XModule.kt` → `val xModule = module { ... }` | `RingtonePickerModule` |
| Domain / data | `XUseCase` (interface) / `XUseCaseImpl` (impl) | `RingtoneProvideUseCase` |
| Repository | `XRepository` (interface) / `MockXRepository` (mock) | `AlarmsRepository` |
| Design-system atom | `Am<Name>`, shared internals `Base<Name>` | `AmFabButton` / `BaseTextButton` |
| Preview | `private`, suffixed `Preview`; fixtures via `previewX()` | `previewRingtones()` |

Known inconsistencies (match what's already in the file you're editing, don't add a
third variant): `feature:alarm-list` mixes `AlarmListState` with `AlarmsListScreenModel`;
`core:ringtone-picker`'s `presentation` package is spelled `presentatoin`
throughout.
