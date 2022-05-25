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
import com.oliverspryn.android.rxjava.model.ObservablesWithSubscribeUiState
import com.oliverspryn.android.rxjava.model.ObservablesWithSubscribeViewModel

@Composable
fun ObservablesWithSubscribe(
    observablesWithSubscribeViewModel: ObservablesWithSubscribeViewModel
) {
    val uiState by observablesWithSubscribeViewModel.uiState.collectAsState()

    ObservablesWithSubscribe(
        observablesWithSubscribeUiState = uiState
    )
}

@Composable
private fun ObservablesWithSubscribe(
    observablesWithSubscribeUiState: ObservablesWithSubscribeUiState
) {
    val text = if (observablesWithSubscribeUiState.error) {
        "There was an error"
    } else if (observablesWithSubscribeUiState.loading) {
        "Loading..."
    } else {
        "Hello, ${observablesWithSubscribeUiState.name}"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = text)
    }
}
