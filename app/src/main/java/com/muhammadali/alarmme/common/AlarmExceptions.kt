package com.muhammadali.alarmme.common

sealed class AlarmExceptions(message: String) : Exception(message) {
    object NotificationPermissionNotGranted : AlarmExceptions("Notifications permission is not granted")
}