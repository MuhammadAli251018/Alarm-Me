package com.muhammadali.alarmme.feature.main.di

import com.muhammadali.alarmme.feature.main.domain.TimeAdapter
import com.muhammadali.alarmme.feature.main.domain.TimeAdapterImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun providesTimeAdapter(): TimeAdapter = TimeAdapterImp()

}