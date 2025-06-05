package com.muhammadali.alarmme.feature.main.di

import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.screen.data.viewmodel.AlarmDataScreenVM
import com.muhammadali.alarmme.feature.main.presentaion.screen.main.viewmodel.MainScreenVM
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory<TimeDateFormatter> {
        TimeDateFormatter()
    }

    viewModel { MainScreenVM(
        alarmsDbRepository = get<AlarmsDBRepo>(),
        alarmScheduler = get<AlarmScheduler>(),
        timeAdapter = get<TimeAdapter>(),
        timeDateFormatter = get<TimeDateFormatter>()
    ) }

    viewModel { AlarmDataScreenVM(
        dbRepository = get<AlarmsDBRepo>(),
        alarmScheduler = get<AlarmScheduler>(),
        timeAdapter = get<TimeAdapter>(),
        timeDateFormatter = get<TimeDateFormatter>()
    ) }
}