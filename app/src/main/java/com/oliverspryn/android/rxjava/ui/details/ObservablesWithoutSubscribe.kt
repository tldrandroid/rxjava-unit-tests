package com.oliverspryn.android.rxjava.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oliverspryn.android.rxjava.model.ObservablesWithoutSubscribeUiState
import com.oliverspryn.android.rxjava.model.ObservablesWithoutSubscribeViewModel

@Composable
fun ObservablesWithoutSubscribe(
    observablesWithoutSubscribeViewModel: ObservablesWithoutSubscribeViewModel
) {
    val uiState by observablesWithoutSubscribeViewModel.uiState.collectAsState()

    // Usually done in a view model
    // But here to prove a point in the unit tests
    observablesWithoutSubscribeViewModel
        .getNumber()
        .subscribe()

    ObservablesWithoutSubscribe(
        observablesWithoutSubscribeUiState = uiState
    )
}

@Composable
private fun ObservablesWithoutSubscribe(
    observablesWithoutSubscribeUiState: ObservablesWithoutSubscribeUiState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "The number is: ${observablesWithoutSubscribeUiState.number}")
    }
}
