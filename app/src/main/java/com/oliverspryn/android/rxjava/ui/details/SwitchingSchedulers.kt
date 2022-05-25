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
import com.oliverspryn.android.rxjava.model.SwitchingSchedulersUiState
import com.oliverspryn.android.rxjava.model.SwitchingSchedulersViewModel

@Composable
fun SwitchingSchedulers(
    switchingSchedulersViewModel: SwitchingSchedulersViewModel
) {
    val uiState by switchingSchedulersViewModel.uiState.collectAsState()

    SwitchingSchedulers(
        switchingSchedulersUiState = uiState
    )
}

@Composable
private fun SwitchingSchedulers(
    switchingSchedulersUiState: SwitchingSchedulersUiState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = switchingSchedulersUiState.name)
        Text(text = switchingSchedulersUiState.devices)
    }
}
