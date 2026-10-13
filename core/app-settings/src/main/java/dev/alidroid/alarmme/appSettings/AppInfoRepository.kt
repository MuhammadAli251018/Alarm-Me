package dev.alidroid.alarmme.appSettings


fun interface AppInfoRepository {
    fun getAppInfo(): AppInfo
}