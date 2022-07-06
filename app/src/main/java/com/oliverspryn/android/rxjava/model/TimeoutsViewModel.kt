package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class TimeoutsViewModel @Inject constructor(
    private val rxJavaFactory: RxJavaFactory,
    private val userProfileRepository: UserProfileRepository,
    private val viewModelState: MutableStateFlow<TimeoutsUiState>
) : ViewModel() {

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
                viewModelState.updateName(profile.name)
            }, {
                viewModelState.setError()
            })
    }

    private fun MutableStateFlow<TimeoutsUiState>.setError() = update {
        it.copy(
            error = true,
            name = ""
        )
    }

    private fun MutableStateFlow<TimeoutsUiState>.updateName(name: String) = update {
        it.copy(
            error = false,
            name = name
        )
    }
}

data class TimeoutsUiState(
    val error: Boolean = false,
    val name: String = ""
)
