package com.oliverspryn.android.rxjava.di.factories.modules

import com.oliverspryn.android.rxjava.model.ObservablesWithSubscribeUiState
import com.oliverspryn.android.rxjava.model.ObservablesWithoutSubscribeUiState
import com.oliverspryn.android.rxjava.model.SchedulersUiState
import com.oliverspryn.android.rxjava.model.SwitchingSchedulersUiState
import com.oliverspryn.android.rxjava.model.TimeoutsUiState
import com.oliverspryn.android.rxjava.model.TimersUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelStateModule {

    @Provides
    @ViewModelScoped
    fun provideObservablesWithoutSubscribeUiState() = MutableStateFlow(
        ObservablesWithoutSubscribeUiState()
    )

    @Provides
    @ViewModelScoped
    fun provideObservableWithSubscribeViewModelState() = MutableStateFlow(
        ObservablesWithSubscribeUiState()
    )

    @Provides
    @ViewModelScoped
    fun provideSchedulersUiState() = MutableStateFlow(
        SchedulersUiState()
    )

    @Provides
    @ViewModelScoped
    fun provideSwitchingSchedulersUiState() = MutableStateFlow(
        SwitchingSchedulersUiState()
    )

    @Provides
    @ViewModelScoped
    fun provideTimeoutsUiState() = MutableStateFlow(
        TimeoutsUiState()
    )

    @Provides
    @ViewModelScoped
    fun provideTimersUiState() = MutableStateFlow(
        TimersUiState()
    )
}
