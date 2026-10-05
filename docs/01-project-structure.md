This file discusses the project structure. After reading through it you should be able to easily
navigate the project, understanding each module's role and the dependencies between them.

## Overview

There are three major module groups declared in the project, each with its own role and dependency
rules relative to the others:

- `:feature` -> Contains the application's features (vertical slices).
A feature represents a screen in the app. A feature cannot depend on another feature,
and can only depend on `core` submodules.
- `:core` -> Contains the application's shared/core logic, used by features and by other core
modules. For example, the design-system, themes, common UI components, navigation, and shared
utilities. Core submodules are broken down by responsibility, not by vertical slice.
- `:app` -> A higher-level consumer module that assembles the other modules and depends on them.
The `app` module doesn't contain any business logic, and only sets up DI and navigation.

## Project Structure

The project's base package is `dev.alidroid.alarmme`. Modules are declared in
`settings.gradle.kts`, and the directory layout mirrors the module graph — the shape of a
directory's path tells you what kind of module it is:

```
app/
feature/
  alarm-list/
  alarm-details/        <- `feature:xxxx`, one per screen, kebab-case
  ...
core/
  mvi/
  navigation/
  preferences/
  app-settings/
  feature-flags/        <- `core:xxxx`, a single-purpose module, solves one concern on its own
  ui/
    theme/
    components/
    screen/
      date-picker/
      ringtone-picker/ 
      ...
  utility/
    compose/
    kotlin-date-time/     <- `core:utility:xxxx`, small dependency-free helpers, split by concern
  alarms/
    domain/
    scheduler/
    data/                  <- declared, but currently a stub (no build.gradle.kts/src/ yet)
    data-mocks/             <- `core:alarms:xxxx`, grouped under the shared alarm domain
```

Two naming shapes show up under `core:`:
- **Flat** — `core:<name>` for a module that solves one concern end-to-end
  (`core:mvi`, `core:navigation`, `core:preferences`, ...).
- **Grouped** — `core:<category>:<name>` once a responsibility splits into several related pieces
  (`core:ui:*`, `core:alarms:*`, `core:utility:*`). Reach for this shape when a flat module's
  internals start separating into more than one clearly distinct piece — don't flatten a group
  back down just to avoid the extra path segment.

## Core

Core modules are responsibility-driven, not vertical slices — each one solves exactly one shared
concern and can be depended on by any feature (or by other core modules) that needs it.

- **Flat modules** solve one concern end-to-end: `core:mvi` (MVI base types — `BaseScreenModel`,
  `State`/`Event`/`Effect`), `core:navigation` (the app's navigation graph), `core:preferences` (a
  shared preferences/DataStore wrapper). `core:app-settings` and `core:feature-flags` are the same
  shape, each building on `core:preferences`.
- **Grouped modules** (`core:<category>:*`) exist where one responsibility splits into several
  pieces:
  - `core:ui:*` — the design system. `theme` holds colors/typography with no dependencies of its
    own; `components` is the shared composables, depending on `theme`; `screen:*` holds
    fully-built, shared screens (e.g. the ringtone picker) that more than one feature needs to
    navigate to — each shaped internally like a feature module.
  - `core:alarms:*` — the alarm domain. `domain` holds the models/use-case interfaces that nearly
    everything else depends on; `scheduler` and `data-mocks` build on top of it; `data` is
    declared but not yet implemented — a known gap, the real persistence layer still needs
    building.
  - `core:utility:*` — small, dependency-free helpers split one-per-concern (e.g. Compose
    extensions, date/time helpers). Add a sibling here rather than growing one into a grab-bag.

A module's own `build.gradle.kts` is the source of truth for its exact dependencies — this section
is about what each group is for and why it's shaped that way, not an exhaustive module list.

## Feature

Each screen is its own module, named `feature:<kebab-case-screen-name>` (e.g.
`feature:alarm-list`, `feature:alarm-details`). A feature depends only on the `core` modules it
needs, and never on another feature — `app` is the only module allowed to depend on more than one
feature at a time.

Every feature module follows the same internal layout:

- `data/`
- `domain/`
- `di/` — a single Koin module wiring up the feature's dependencies.
- `presentation/` — the `Screen` and `ScreenModel`, with nested `component/` and `model/`
  packages.

## App

`:app` is the application module (`android.application` plugin) and the root of the dependency
graph. It assembles every feature module, plus whichever `core:*` modules it needs directly for its own setup (DI wiring, shared
navigation screens it needs to register, and the like).

It's responsible for organizing and building the project's DI, 
like picking mocks vs production implementations depending on which flavor of the app is running.

`app` contains no business logic of its own. Its only two responsibilities are:

- Setting up dependency injection (Koin) for the whole app.
- Hosting the root Voyager `Navigator`.

## Dependency Diagram

The high-level layering is strict: `app` depends on `feature:*` and `core:*`; `feature:*` depends
only on `core:*`; `core:*` modules may depend on other `core:*` modules, but never on `feature:*`.
Features never depend on each other.

```
                      ┌─────┐
                      │ app │
                      └──┬──┘
             ┌───────────┼───────────┐
             ▼           ▼           ▼
     feature:alarm-list  ...   feature:debug-panel
             │           │           │
             └───────────┼───────────┘
                          ▼
                       core:*
```

Within `core:*`, dependencies flow from grouped/composite modules down to flatter, lower-level
ones — e.g. a `core:ui:screen:*` depends on `core:ui:components`, which depends on
`core:ui:theme`; `core:alarms:scheduler` depends on `core:alarms:domain`. `core:alarms:domain`
sits near the very bottom of the graph: it's depended on by most features and several core
modules alike, since it holds the shared alarm model everything else builds on.
