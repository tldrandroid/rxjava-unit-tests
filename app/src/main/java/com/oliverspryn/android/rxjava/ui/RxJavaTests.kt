package com.oliverspryn.android.rxjava.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oliverspryn.android.rxjava.model.*
import com.oliverspryn.android.rxjava.ui.details.*
import com.oliverspryn.android.rxjava.ui.theme.RxJavaUnitTestsTheme

@Composable
fun RxJavaTests() {
    RxJavaUnitTestsTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(
                    onNoSubscribeTap = { navController.navigate("no-subscribe") },
                    onEncapsulatedSubscribeTap = { navController.navigate("with-subscribe") },
                    onSchedulersTap = { navController.navigate("schedulers") },
                    onSwitchingSchedulersTap = { navController.navigate("switching-schedulers") },
                    onTimeoutsTap = { navController.navigate("timeouts") },
                    onTimersDelaysIntervalsTap = { navController.navigate("timers-delays-intervals") }
                )
            }

            composable("no-subscribe") {
                val viewModel: ObservablesWithoutSubscribeViewModel = hiltViewModel()

                ObservablesWithoutSubscribe(
                    observablesWithoutSubscribeViewModel = viewModel
                )
            }

            composable("with-subscribe") {
                val viewModel: ObservablesWithSubscribeViewModel = hiltViewModel()

                ObservablesWithSubscribe(
                    observablesWithSubscribeViewModel = viewModel
                )
            }

            composable("schedulers") {
                val viewModel: SchedulersViewModel = hiltViewModel()

                Schedulers(
                    schedulersViewModel = viewModel
                )
            }

            composable("switching-schedulers") {
                val viewModel: SwitchingSchedulersViewModel = hiltViewModel()

                SwitchingSchedulers(
                    switchingSchedulersViewModel = viewModel
                )
            }

            composable("timeouts") {
                val viewModel: TimeoutsViewModel = hiltViewModel()

                Timeouts(
                    timeoutsViewModel = viewModel
                )
            }

            composable("timers-delays-intervals") {
                val viewModel: TimersViewModel = hiltViewModel()

                Timers(
                    timersViewModel = viewModel
                )
            }
        }
    }
}
