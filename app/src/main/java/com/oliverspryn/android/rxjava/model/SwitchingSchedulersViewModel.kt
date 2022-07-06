package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class SwitchingSchedulersViewModel @Inject constructor(
    private val rxJavaFactory: RxJavaFactory,
    private val userProfileRepository: UserProfileRepository,
    private val viewModelState: MutableStateFlow<SwitchingSchedulersUiState>
) : ViewModel() {

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
                viewModelState.updateName(profile.name)
                Completable.complete()
            }
            .observeOn(rxJavaFactory.io)
            .andThen(userProfileRepository.getActiveLogins())
            .observeOn(rxJavaFactory.ui)
            .subscribe({ devices ->
                val commaSeparatedDeviceList = devices.joinToString(", ") { it.deviceName }
                viewModelState.updateDevices(commaSeparatedDeviceList)
            }, {

            })
    }

    private fun MutableStateFlow<SwitchingSchedulersUiState>.updateDevices(devices: String) =
        update {
            it.copy(
                devices = devices
            )
        }

    private fun MutableStateFlow<SwitchingSchedulersUiState>.updateName(name: String) = update {
        it.copy(
            name = name
        )
    }
}

data class SwitchingSchedulersUiState(
    val devices: String = "",
    val name: String = ""
)
