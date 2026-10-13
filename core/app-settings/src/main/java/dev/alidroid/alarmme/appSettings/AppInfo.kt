package dev.alidroid.alarmme.appSettings

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val appVersion: String,
    val appVersionCode: Int,
    val gitHashName: String
)