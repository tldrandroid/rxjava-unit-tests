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
import com.oliverspryn.android.rxjava.model.TimeoutsUiState
import com.oliverspryn.android.rxjava.model.TimeoutsViewModel

@Composable
fun Timeouts(
    timeoutsViewModel: TimeoutsViewModel
) {
    val uiState by timeoutsViewModel.uiState.collectAsState()

    Timeouts(
        timeoutsUiState = uiState
    )
}

@Composable
private fun Timeouts(
    timeoutsUiState: TimeoutsUiState
) {
    val text = if (timeoutsUiState.error) {
        "Request timed out"
    } else {
        timeoutsUiState.name
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text)
    }
}
