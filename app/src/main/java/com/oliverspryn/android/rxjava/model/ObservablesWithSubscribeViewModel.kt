package com.oliverspryn.android.rxjava.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverspryn.android.rxjava.data.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class ObservablesWithSubscribeViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val viewModelState: MutableStateFlow<ObservablesWithSubscribeUiState>
) : ViewModel() {

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ObservablesWithSubscribeUiState()
        )

    init {
        getProfile()
    }

    @VisibleForTesting
    fun getProfile() {
        userProfileRepository
            .getUserProfile()
            .flatMap { profile -> Single.just(profile.name) }
            .doOnSubscribe { viewModelState.setLoading() }
            .doOnError { viewModelState.setError() }
            .subscribe({ name ->
                viewModelState.setSuccess(name)
            }, {

            })
    }

    private fun MutableStateFlow<ObservablesWithSubscribeUiState>.setError() = update {
        it.copy(
            error = true,
            loading = false,
            name = ""
        )
    }

    private fun MutableStateFlow<ObservablesWithSubscribeUiState>.setLoading() = update {
        it.copy(
            error = false,
            loading = true,
            name = ""
        )
    }

    private fun MutableStateFlow<ObservablesWithSubscribeUiState>.setSuccess(name: String) =
        update {
            it.copy(
                error = false,
                loading = false,
                name = name
            )
        }
}

data class ObservablesWithSubscribeUiState(
    val error: Boolean = false,
    val loading: Boolean = false,
    val name: String = ""
)
