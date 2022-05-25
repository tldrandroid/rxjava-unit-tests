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
import com.oliverspryn.android.rxjava.model.TimersUiState
import com.oliverspryn.android.rxjava.model.TimersViewModel

@Composable
fun Timers(
    timersViewModel: TimersViewModel
) {
    val uiState by timersViewModel.uiState.collectAsState()

    Timers(
        timersUiState = uiState
    )
}

@Composable
private fun Timers(
    timersUiState: TimersUiState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "The number is: ${timersUiState.number}")
    }
}
