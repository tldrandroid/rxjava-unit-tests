package com.oliverspryn.android.rxjava.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class ObservablesWithoutSubscribeViewModelTest : DescribeSpec({
    describe("The ObservablesWithoutSubscribeViewModel") {
        val viewModelState = MutableStateFlow(ObservablesWithoutSubscribeUiState())
        val intermediateStates = mutableListOf<ObservablesWithoutSubscribeUiState>()

        val uut = ObservablesWithoutSubscribeViewModel(
            viewModelState
        )

        describe("when getNumber") {
            runTest {
                var didError = false
                var didSucceed = false
                var givenNumber = 0

                val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModelState.collect {
                        intermediateStates.add(it)
                    }
                }

                intermediateStates.clear() // Clear off value emitted by initial state

                uut
                    .getNumber()
                    .subscribe({
                        didSucceed = true
                        givenNumber = it
                    }, {
                        didError = true
                    })

                job.cancel()

                it("succeeded with the number 4") {
                    didError shouldBe false
                    didSucceed shouldBe true
                    givenNumber shouldBe 4
                }

                it("updates the state to 4") {
                    intermediateStates.size shouldBe 1

                    intermediateStates[0] shouldBe ObservablesWithoutSubscribeUiState(
                        number = 4
                    )
                }
            }
        }
    }
})
