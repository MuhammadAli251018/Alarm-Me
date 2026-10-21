# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.0-beta] - 2026-10-21
### Added
- The app now asks for notification permission when it launches. Alarms ring through a
    notification, so they stay silent without it.

### Changed
- User-facing text now comes from string resources rather than being hardcoded, so the app can be
    translated. No translations ship yet.
- The date picker's selected-date count reads "1 DATE SELECTED" rather than "1 DATES SELECTED".

### Fixed
- Right-to-left layouts now mirror correctly. Back arrows and chevrons point toward the start edge,
    the alarm toggle's thumb stays on its track, the hold-to-dismiss sweep fills from the start
    edge, the two-pane alarm details divider sits between the panes, and the ringing screen's glow
    follows the content.
- Times keep their left-to-right reading order in right-to-left locales, so a clock reads
    "6:40 AM" rather than "AM 6:40".
- Debug Panel is accessible in production flavor.
- Alarms now ring. When an alarm goes off its ringtone plays and the device vibrates until the
    alarm is dismissed. Vibration follows the alarm's own vibration setting.
- Other audio pauses while an alarm is ringing.

## [0.1.0-beta] - 2026-10-16

Initial release 🎉

[Unreleased]: https://github.com/MuhammadAli251018/Alarm-Me/compare/v0.2.0-beta...HEAD
[0.2.0-beta]: https://github.com/MuhammadAli251018/Alarm-Me/releases/tag/v0.2.0-beta
[0.1.0-beta]: https://github.com/MuhammadAli251018/Alarm-Me/releases/tag/v0.1.0-beta
