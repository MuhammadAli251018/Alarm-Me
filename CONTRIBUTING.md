# Contributing to Alarm Me

Thanks for your interest in contributing. This document covers how to build the project, run its
tests, and the conventions the codebase follows. For what the app actually does, see
[README.md](README.md).

## Building the project

```bash
git clone <your-fork-url>
cd Alarm-Me
./gradlew :app:assembleDebug
```

Or open the project root in Android Studio and run the `app` configuration directly.

## Running tests

```bash
# Run all unit tests
./gradlew test

# Run tests for a single module (e.g. the alarm domain)
./gradlew :core:alarms:domain:test

# Run a single test class or spec (Kotest, matched by class name)
./gradlew :core:alarms:domain:test --tests "dev.alidroid.alarmme.alarms.domain.AlarmOccurrenceTest"
```

Unit tests use Kotest (`StringSpec`) on the JUnit Platform. There's no CI configured yet, so make
sure tests pass locally before opening a PR.

## Architecture

The project follows Clean Architecture with feature modularization: `app` is a thin assembler,
`feature:*` modules are vertical slices (one screen each, never depending on another feature),
and `core:*` modules are shared, responsibility-driven building blocks (design system,
navigation, MVI base types, the alarm domain, etc.) that any feature can depend on.

Before making structural changes, read the project's own docs — they're the source of truth and
this file won't repeat them:

- [`docs/00-philosophy.md`](docs/00-philosophy.md) — the five-minute overview, start here.
- [`docs/01-project-structure.md`](docs/01-project-structure.md) — the module graph and
  dependency rules.
- [`docs/02-mvi.md`](docs/02-mvi.md) — the MVI contract and the Voyager navigation layer.
- [`docs/03-feature-anatomy.md`](docs/03-feature-anatomy.md) — what goes inside a feature
  module's `presentation/` package.
- [`docs/04-ui-conventions.md`](docs/04-ui-conventions.md) — the design system and preview
  conventions.
- [`docs/05-testing-strategy.md`](docs/05-testing-strategy.md) — what's worth unit testing and
  why previews substitute for Compose UI tests here.

## Before opening a PR

- Run the relevant tests locally (see above) — there's no CI to catch regressions for you yet.
- Follow the module and naming conventions in the docs linked above rather than introducing new
  patterns.

## License

By contributing, you agree that your contributions will be licensed under the project's
[MIT License](LICENSE).
