# Philosophy of the Alarm-Me Project

This file is meant to be an introduction to the project, covering the philosophies behind its structure and decisions. It contains mostly overview-level, high-level guidance — for more detail and examples, see the dedicated document for each section.

## 1. Feature Modularization & Clean Architecture

The project follows Clean Architecture, combined with feature modularization as recommended by Google. It breaks down into Gradle modules, each with a single responsibility. There are three major module types that everything else falls under:

- `feature:xxx` — a feature module, representing a single screen.
- `core:xxx` — responsibility-driven modules defining common tools and solutions for feature modules to consume.
- `app` — the main application module, acting as a thin, high-level consumer.

*See: `01-project-structure.md`*

## 2. Testing Strategy

The project's tests focus on unit testing using Equivalence Partitioning. For UI, we depend on Compose Previews over each possible state, rather than end-to-end Compose UI tests.

In short:

- Not every implementation needs testing — only logic-bearing units should be tested.
- Compose Previews over end-to-end UI tests.
- Unit tests should be compact, straightforward to read, and follow Equivalence Partitioning.

*See: `05-testing-strategy.md`*

## 3. Mocks & Development Environment

Mock implementations provide a convenient way to quickly, manually test the app. A dedicated `debug-panel` screen contains helpful tools for developers, along with feature flags for mocking and tweaking the app's behavior.

Mocks only exist in the `dev` app flavor — only production code is shipped in `prod`.

## 4. MVI

- Every screen follows the MVI architectural pattern. Every screen model inherits `BaseScreenModel`, which implements the core MVI structure.
- `State` represents the current UI state, `Event` represents a user action (also referred to as Intent elsewhere), and `Effect` represents a fired, one-off event.

*See: `02-mvi.md`*

## 5. Voyager Navigation

- The project uses Voyager as its navigation library, binding each Compose screen to a `Screen` class.
- `core:navigation` contains the app's core navigation logic.

## 6. UI Construction

- The UI has its own design system, independent from Material3. The app theme lives in `core:ui:theme`, while custom components live in `core:ui:components`.
- Every component, whether in `core` or feature-specific, needs a preview covering all of its different states.
- Every UI screen needs previews covering all screen sizes (the screen category API makes managing different sizes convenient) × all different states.
- Every UI screen has a stateless `XXXScreenContent` composable containing the actual screen content, used to demonstrate all the different states.

*See: `04-ui-conventions.md`*