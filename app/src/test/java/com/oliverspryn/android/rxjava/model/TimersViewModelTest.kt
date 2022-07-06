package com.oliverspryn.android.rxjava.model

import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.schedulers.TestScheduler
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class TimersViewModelTest : DescribeSpec({
    describe("The TimersViewModel") {
        val rxJavaFactory: RxJavaFactory = mockk()
        val viewModelState = MutableStateFlow(TimersUiState())
        val intermediateStates = mutableListOf<TimersUiState>()

        val ioScheduler = TestScheduler()
        every { rxJavaFactory.io } returns ioScheduler

        val uut = TimersViewModel(
            rxJavaFactory,
            viewModelState
        )

        describe("when getNumber") {
            runTest {
                val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModelState.collect {
                        intermediateStates.add(it)
                    }
                }

                intermediateStates.clear() // Clear off value emitted by initial state
                uut.getNumber()
                ioScheduler.advanceTimeBy(6L, TimeUnit.SECONDS)
                job.cancel()

                it("updates the number") {
                    intermediateStates.size shouldBe 1

                    intermediateStates[0] shouldBe TimersUiState(
                        number = 42
                    )
                }
            }
        }
    }
})
