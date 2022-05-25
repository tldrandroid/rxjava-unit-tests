package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SwitchingSchedulersViewModel @Inject constructor(
    private val rxJavaFactory: RxJavaFactory,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(SwitchingSchedulersUiState())

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            SwitchingSchedulersUiState()
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
            .flatMapCompletable { profile ->
                viewModelState.update { state ->
                    state.copy(
                        name = profile.name
                    )
                }

                Completable.complete()
            }
            .observeOn(rxJavaFactory.io)
            .andThen(userProfileRepository.getActiveLogins())
            .observeOn(rxJavaFactory.ui)
            .subscribe({ devices ->
                viewModelState.update { state ->
                    state.copy(
                        devices = devices.joinToString(", ") { it.deviceName }
                    )
                }
            }, {

            })
    }
}

data class SwitchingSchedulersUiState(
    val devices: String = "",
    val name: String = ""
)
