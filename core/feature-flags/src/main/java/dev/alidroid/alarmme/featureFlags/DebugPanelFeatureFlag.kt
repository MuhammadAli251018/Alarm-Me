package dev.alidroid.alarmme.featureFlags

enum class DebugPanelFeatureFlag { Enable, Disable }

fun DebugPanelFeatureFlag.isEnabled(): Boolean = when (this) {
    DebugPanelFeatureFlag.Enable -> true
    DebugPanelFeatureFlag.Disable -> false
}
