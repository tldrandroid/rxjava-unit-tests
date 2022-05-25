package com.oliverspryn.android.rxjava.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ObservablesWithoutSubscribeViewModel @Inject constructor() : ViewModel() {

    private val viewModelState = MutableStateFlow(ObservablesWithoutSubscribeUiState())

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ObservablesWithoutSubscribeUiState()
        )

    fun getNumber(): Single<Int> = Single
        .just(2)
        .flatMap { number ->
            val output = number * 2

            viewModelState.update {
                it.copy(
                    number = output
                )
            }

            Single.just(output)
        }
}

data class ObservablesWithoutSubscribeUiState(
    val number: Int = 0
)
