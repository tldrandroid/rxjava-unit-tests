package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TimersViewModel @Inject constructor(
    private val rxJavaFactory: RxJavaFactory
) : ViewModel() {

    private val viewModelState = MutableStateFlow(TimersUiState())

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            TimersUiState()
        )

    init {
        getNumber()
    }

    @VisibleForTesting
    fun getNumber() {
        Single
            .timer(5L, TimeUnit.SECONDS, rxJavaFactory.io)
            .flatMap {
                Single.just(42)
            }
            .subscribe({ output ->
                viewModelState.update {
                    it.copy(
                        number = output
                    )
                }
            }, {

            })
    }
}

data class TimersUiState(
    val number: Int = 0
)
