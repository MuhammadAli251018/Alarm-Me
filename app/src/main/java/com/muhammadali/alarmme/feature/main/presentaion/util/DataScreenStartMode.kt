package com.muhammadali.alarmme.feature.main.presentaion.util

sealed class DataScreenStartMode(val data: Int){
    object CreateNewAlarmMode : DataScreenStartMode(-1)
    data class UpdateAlarmMode(val id: Int) : DataScreenStartMode(id)
}
