# UI Conventions

This file covers the design system and the conventions around building UI with it: where
components live, how UI state is named, the stateless content pattern, preferring hoisted state
over local Compose state, and previews.

## Design System

- The UI has its own design system, independent of Material3 — no `MaterialTheme`.
- `core:ui:theme` holds the theme: colors, typography, shapes, dimensions. Read via
  `LocalAlarmMeColors.current` (and sibling `LocalAlarmMeTypography`/`LocalAlarmMeShapes`/
  `LocalAlarmMeDimensions`), or the `AlarmMeTheme.colors`-style convenience accessor.
- `core:ui:components` holds shared, reusable composables — the design-system atoms.
- Placement rule: a composable starts in its own feature's `presentation/component/` package. It
  only gets promoted to `core:ui:components` once a second screen actually needs it.

## Naming

- Shared design-system composables use an `Am<Name>` prefix (`AmSectionCard`, `AmSwitchToggle`,
  `AmBadge`, `AmCheckBox`, `AmFabButton`). Where several `Am*` variants share an internal
  implementation, that shared piece is named `Base<Name>` and kept `internal` — e.g.
  `BaseFabButton` backs `AmFabButton`/`AmFabSmallButton`/`AmFabExtendedButton`.
- UI-facing state classes are suffixed `State` (e.g. `AlarmItemState`)

## Stateless Screen Content

Every screen has a stateless `XScreenContent` composable holding the real UI, separate from the
`Screen.kt`/`ScreenModel` wiring (*see `03-feature-anatomy.md`*). This composable is what drives
both the real screen and every one of its previews — if something UI-visible only exists inside
`Screen.Content()`, it can't be previewed.

## Previews

- Every component needs a preview covering every one of its states (loading/loaded/error,
  selected/unselected, enabled/disabled, etc).
- Every screen needs previews covering all screen sizes × all states — not just the default size.
- `core:ui:theme`'s screen-category API makes the size axis manageable without hand-writing a
  preview per bucket:
  - `currentScreenCategory()` — a composable reading the real window size at runtime.
  - `@CommonScreenCategoriesPreview` — the three buckets most real devices land in, in both
    themes (6 renders). The everyday default for previews.
  - `@AllScreenCategoriesPreview` — all 15 width × height buckets in both themes (30 renders) —
    exhaustive and slow; reach for it when a layout's correctness genuinely depends on screen
    size, not by default.
- `@RtlPreview` is a separate, opt-in axis: one compact bucket in both themes with `locale = "ar"`,
  so the layout renders right-to-left (2 renders). It is deliberately *not* folded into the two
  annotations above, which would double their render counts. Add it to a screen, and to any
  component whose layout actually mirrors — it is the only place RTL gets verified, since previews
  stand in for UI tests here.
