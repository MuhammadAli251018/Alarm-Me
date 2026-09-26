This is Alarm Me — a modern Android Alarm Clock app built with Jetpack Compose, using
multi-module Gradle, feature modularization, and an MVI architecture (Voyager for
navigation/screen models, Koin for DI).

For the module structure, dependency rules, MVI/component conventions, and naming
conventions, see `docs/project-structures.md` — read it before adding a module,
screen, or component.

While working in the project, don't add documentation unless explicitly requested to.

When implementing UI, don't improvise new styles, colors, or typography. All UI
components must be derived from the design system:
1. Use a direct match from the design system if one exists.
2. If there is no direct match, use the closest existing approximation.
3. If even the closest approximation is too far off, copy it locally and adjust
   only its typography.
