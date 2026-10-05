# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build everything
./gradlew build

# Assemble the app only
./gradlew :app:assembleDebug

# Run all unit tests
./gradlew test

# Run tests for a single module (e.g. the alarm domain)
./gradlew :core:alarms:domain:test

# Run a single test class or spec (Kotest, matched by class name)
./gradlew :core:alarms:domain:test --tests "dev.alidroid.alarmme.alarms.domain.AlarmOccurrenceTest"
```

Unit tests use Kotest (`StringSpec`) running on JUnit Platform — there is no detekt/ktlint setup in this repo.

## Architecture

Full detail lives in `docs/00-philosophy.md` through `docs/05-testing-strategy.md` — read those
before making structural changes. This section is only the map between them.

- **`docs/00-philosophy.md`** — the five-minute overview; start here.
- **`docs/01-project-structure.md`** — module graph and dependency rules (`app` / `feature:*` /
  `core:*`, flat vs. grouped `core` modules).
- **`docs/02-mvi.md`** — the MVI contract (`BaseScreenModel`, `State`/`Event`/`Effect`) and the
  Voyager navigation layer (`NavDestination`, `NavigationGraph`, nav results).
- **`docs/03-feature-anatomy.md`** — what goes inside a feature's `presentation/` package
  (`component/` vs `model/`, the Mapper, `Screen`/`ScreenModel`/`XScreenContent`).
- **`docs/04-ui-conventions.md`** — the design system (`core:ui:theme`, `core:ui:components`,
  `Am*` naming) and the preview conventions (`@CommonScreenCategoriesPreview` /
  `@AllScreenCategoriesPreview`).
- **`docs/05-testing-strategy.md`** — what's worth unit testing, equivalence partitioning, and why
  previews substitute for Compose UI tests here.

Key things that cut across multiple files and are easy to miss on a first pass:

- A **feature** (`feature:<kebab-case-screen>`) is one screen. Features never depend on each
  other — only `app` depends on more than one feature at a time.
- **`core:*`** is responsibility-driven, not vertical-slice. `core:alarms:domain` sits near the
  bottom of the dependency graph — most features and several other core modules depend on it.
  `core:alarms:data` is declared in `settings.gradle.kts` but not yet implemented (no
  `build.gradle.kts`/`src` yet); mocked data comes from `core:alarms:data-mocks` in the meantime.
  `core:preferences` is the base DataStore wrapper that `core:app-settings` and
  `core:feature-flags` build on.
- One-off UI reactions (snackbar, "navigate back now") are modeled as MVI `Effect`s, never as a
  boolean/nullable field in `State` — this is what keeps that behavior unit-testable without a
  Compose UI test harness.
- Cross-feature navigation never imports the target feature directly; it goes through
  `core:navigation`'s `NavDestination` + `NavigationGraph` indirection. Passing data back from a
  pushed screen (e.g. the ringtone picker) goes through `NavResultRepository`/`AwaitNavResult`,
  not through `BaseScreenModel` state.
