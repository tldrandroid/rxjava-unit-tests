package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TimeoutsViewModel @Inject constructor(
    private val rxJavaFactory: RxJavaFactory,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(TimeoutsUiState())

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            TimeoutsUiState()
        )

    init {
        getAllUserData()
    }

    @VisibleForTesting
    fun getAllUserData() {
        userProfileRepository
            .getUserProfileLongWait()
            .timeout(5L, TimeUnit.SECONDS, rxJavaFactory.io)
            .subscribeOn(rxJavaFactory.io)
            .observeOn(rxJavaFactory.ui)
            .subscribe({ profile ->
                viewModelState.update { state ->
                    state.copy(
                        error = false,
                        name = profile.name
                    )
                }
            }, {
                viewModelState.update { state ->
                    state.copy(
                        error = true
                    )
                }
            })
    }
}

data class TimeoutsUiState(
    val error: Boolean = false,
    val name: String = ""
)
