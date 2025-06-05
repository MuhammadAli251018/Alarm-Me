package com.muhammadali.alarmme.feature.allAlarms.di

import com.muhammadali.alarmme.feature.allAlarms.presentation.AllAlarmsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val allAlarmsModule = module{
    viewModel<AllAlarmsViewModel> { AllAlarmsViewModel() }
}