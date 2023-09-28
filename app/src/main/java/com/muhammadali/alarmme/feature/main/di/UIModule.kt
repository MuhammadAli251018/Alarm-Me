package com.muhammadali.alarmme.feature.main.di

import com.muhammadali.alarmme.feature.main.ui.screen.main.viewmodel.MainUIState
import com.muhammadali.alarmme.feature.main.ui.util.TimeDateFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object UIModule {

    @Provides
    fun providesInitialMainUIState() = MainUIState(listOf())

    @Provides
    fun providesTimeFormatter(): TimeDateFormatter {
        return TimeDateFormatter()
    }
}