package com.muhammadali.alarmme.feature.allAlarms.di

import com.muhammadali.alarmme.feature.allAlarms.presentation.AllAlarmsViewModel
import com.muhammadali.alarmme.feature.main.domain.entities.AlarmScheduler
import com.muhammadali.alarmme.feature.main.domain.entities.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.repositories.AlarmsDBRepo
import com.muhammadali.alarmme.feature.main.presentaion.util.TimeDateFormatter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val allAlarmsModule = module{
    viewModel<AllAlarmsViewModel> { AllAlarmsViewModel(
        alarmsDbRepository = get<AlarmsDBRepo>(),
        alarmScheduler = get<AlarmScheduler>(),
        timeAdapter = get<TimeAdapter>(),
        timeDateFormatter = get<TimeDateFormatter>()
    ) }
}