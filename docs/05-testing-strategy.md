# 05 — Testing Strategy

## Overview

A test is only worth writing if it protects real logic that could realistically break.
We lean heavily on **Compose Previews** instead of Compose UI tests for visual verification,
and we deliberately avoid writing many near-duplicate test cases that all exercise the same behavior.

---

## When to Write a Test

The core rule is a cost/value trade-off: a test has a maintenance price, and that price must be paid back in the value it provides. 
Logic-free code (plain factories, simple data holders) is generally **not** worth testing unless it secretly contains something non-trivial.

Guidance by layer:

| Layer | Should you test it? |
|---|---|
| **Custom state holders** (components managing real internal logic) | Yes — these need direct unit tests verifying behavior. |
| **Use cases** | High priority. This is where business logic concentrates, so it deserves careful consideration for testing. |
| **Repositories** | Only if the I/O logic itself has genuine complexity. Simple pass-through repositories don't need tests. |
| **Screen Models** | Yes, when they manage meaningful state, several events, and effects. A trivial screen model with barely any logic doesn't need one. |
| **Factories / simple data holders / mappers with no real logic** | Generally skip, unless there's hidden complexity worth protecting. |

**Examples of what should be tested:**

- Use cases containing real business logic
- Formatters & mapping functions with non-trivial logic (e.g. time formatting, data transformations) — not simple 1:1 passthroughs
- Complex state holders — custom state-holder classes managing real internal logic
- Screen Models with complex state, many events, or effects
- Repositories — only when the I/O logic itself has genuine complexity beyond simple pass-through

---

## How We Write Tests: Equivalence Partitioning

We avoid redundant test cases that fall into the same behavioral category. For example, for a function that adds two numbers, we don't need separate tests for `1 + 1`, `1 + 5`, and `1 + 10` — they all exercise the same behavior.

Instead:
- Write **one representative case per equivalence class** (e.g., one "normal case" test).
- Separately test **boundaries and edge cases** that belong to a genuinely different class — `0`, negative numbers, `infinity`, etc.

This is a known technique called **equivalence partitioning**, usually paired with **boundary value analysis** for the edge cases specifically. Naming it this way in code reviews and docs keeps the convention precise rather than "vibes based."

---

## Designing for Testability

Good screen models are designed to be test-friendly from the start. A common anti-pattern is keeping transient UI state (like whether a dialog is shown) purely as local `remember` state inside a composable. 
It's convenient, but it means that behavior can't be unit tested without resorting to full Compose UI tests, which we want to avoid (see below).

**Prefer:** moving that kind of state into an `Effect`, so the behavior is verifiable with a plain unit test against the screen model, with no UI test harness required.

---

## Previews Instead of UI Tests

We avoid Compose UI tests as much as possible. Instead, we rely extensively on **Previews** as our primary tool for visual verification.

Important distinction: **Previews are not a substitute for tests.** A preview gives you no assertion — it's a visual check with your own eyes, not an automated pass/fail. That said, for the large majority of UI verification needs, a preview is enough, and it's far cheaper to maintain than a Compose UI test.

**Preview rules:**
- **Screens** must have a preview for each relevant screen size category (e.g. phone, tablet) — this is not optional, it's automatic for any screen.
- Whenever a **state change meaningfully alters what's rendered** (e.g. a button becomes disabled, an error state appears), that state needs its own preview, across the relevant screen sizes.
- **Components** only need multiple previews when their appearance actually changes across state or screen size — no need to preview a component that never changes.

To keep this from being a burden, `core:ui:theme` already provides a **screen category API** with preview utilities — `@CommonScreenCategoriesPreview` (the common-device default, 6 renders) and `@AllScreenCategoriesPreview` (every screen-size bucket, exhaustive, 30 renders) fan out the size axis automatically, so you don't need to hand-write dozens of preview functions per composable. See `04-ui-conventions.md` for the exact names and when to reach for which.