package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class SchedulersViewModel @Inject constructor(
    private val rxJavaFactory: RxJavaFactory,
    private val userProfileRepository: UserProfileRepository,
    private val viewModelState: MutableStateFlow<SchedulersUiState>
) : ViewModel() {

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            SchedulersUiState()
        )

    init {
        getAllUserData()
    }

    @VisibleForTesting
    fun getAllUserData() {
        userProfileRepository
            .getUserProfile()
            .subscribeOn(rxJavaFactory.io)
            .observeOn(rxJavaFactory.ui)
            .subscribe({ profile ->
                viewModelState.updateName(profile.name)
            }, {

            })
    }

    private fun MutableStateFlow<SchedulersUiState>.updateName(name: String) = update {
        it.copy(
            name = name
        )
    }
}

data class SchedulersUiState(
    val name: String = ""
)
