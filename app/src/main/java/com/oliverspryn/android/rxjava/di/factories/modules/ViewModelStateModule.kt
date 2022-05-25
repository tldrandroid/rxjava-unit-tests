package com.oliverspryn.android.rxjava.di.factories.modules

import com.oliverspryn.android.rxjava.model.ObservablesWithSubscribeUiState
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
    fun provideObservableWithSubscribeViewModelState() = MutableStateFlow(
        ObservablesWithSubscribeUiState()
    )
}
