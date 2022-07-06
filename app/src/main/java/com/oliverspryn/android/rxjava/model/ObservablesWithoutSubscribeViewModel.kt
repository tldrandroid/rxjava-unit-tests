package com.oliverspryn.android.rxjava.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class ObservablesWithoutSubscribeViewModel @Inject constructor(
    private val viewModelState: MutableStateFlow<ObservablesWithoutSubscribeUiState>
) : ViewModel() {

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
            viewModelState.updateNumber(output)

            Single.just(output)
        }

    private fun MutableStateFlow<ObservablesWithoutSubscribeUiState>.updateNumber(number: Int) =
        update {
            it.copy(
                number = number
            )
        }
}

data class ObservablesWithoutSubscribeUiState(
    val number: Int = 0
)
