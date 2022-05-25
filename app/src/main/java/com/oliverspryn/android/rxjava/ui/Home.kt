package com.oliverspryn.android.rxjava.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.oliverspryn.android.rxjava.ui.theme.RxJavaUnitTestsTheme

@Composable
fun Home(
    onNoSubscribeTap: () -> Unit,
    onEncapsulatedSubscribeTap: () -> Unit,
    onSchedulersTap: () -> Unit,
    onSwitchingSchedulersTap: () -> Unit,
    onTimeoutsTap: () -> Unit,
    onTimersDelaysIntervalsTap: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onNoSubscribeTap) {
            Text("Observables Without subscribe()")
        }

        Button(onClick = onEncapsulatedSubscribeTap) {
            Text("Observables With subscribe()")
        }

        Button(onClick = onSchedulersTap) {
            Text("Schedulers")
        }

        Button(onClick = onSwitchingSchedulersTap) {
            Text("Switching Schedulers")
        }

        Button(onClick = onTimeoutsTap) {
            Text("Timeouts")
        }

        Button(onClick = onTimersDelaysIntervalsTap) {
            Text("Timers + Delays + Intervals")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    RxJavaUnitTestsTheme {
        Home(
            onNoSubscribeTap = { },
            onEncapsulatedSubscribeTap = { },
            onSchedulersTap = { },
            onSwitchingSchedulersTap = { },
            onTimeoutsTap = { },
            onTimersDelaysIntervalsTap = { }
        )
    }
}
