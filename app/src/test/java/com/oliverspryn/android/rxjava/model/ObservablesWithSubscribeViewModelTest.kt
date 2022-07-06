package com.oliverspryn.android.rxjava.model

import com.oliverspryn.android.rxjava.data.UserProfileRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class ObservablesWithSubscribeViewModelTest : DescribeSpec({
    describe("The ObservablesWithSubscribeViewModel") {
        val userProfileRepository: UserProfileRepository = mockk()
        val viewModelState = MutableStateFlow(ObservablesWithSubscribeUiState())
        val intermediateStates = mutableListOf<ObservablesWithSubscribeUiState>()

        every { userProfileRepository.getUserProfile() } returns Single.just(
            UserProfileRepository.UserInfo(
                email = "john.smith@gmail.com",
                name = "John Smith",
                nickName = "Johnny",
                picture = "https://www.fillmurray.com/300/300"
            )
        )

        val uut = ObservablesWithSubscribeViewModel(
            userProfileRepository,
            viewModelState
        )

        describe("when getProfile") {
            describe("and the call succeeds") {
                runTest {
                    val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                        viewModelState.collect {
                            intermediateStates.add(it)
                        }
                    }

                    intermediateStates.clear() // Clear off value emitted by initial state
                    uut.getProfile()
                    job.cancel()

                    it("updates the state to loading, then success with the user's name") {
                        intermediateStates.size shouldBe 2

                        intermediateStates[0] shouldBe ObservablesWithSubscribeUiState(
                            error = false,
                            loading = true,
                            name = ""
                        )

                        intermediateStates[1] shouldBe ObservablesWithSubscribeUiState(
                            error = false,
                            loading = false,
                            name = "John Smith"
                        )
                    }
                }
            }

            describe("and the call does not succeed") {
                runTest {
                    every { userProfileRepository.getUserProfile() } returns Single.error(
                        IllegalStateException()
                    )

                    val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                        viewModelState.collect {
                            intermediateStates.add(it)
                        }
                    }

                    intermediateStates.clear() // Clear off value emitted by initial state
                    uut.getProfile()
                    job.cancel()

                    it("updates the state to loading, then error") {
                        intermediateStates.size shouldBe 2

                        intermediateStates[0] shouldBe ObservablesWithSubscribeUiState(
                            error = false,
                            loading = true,
                            name = ""
                        )

                        intermediateStates[1] shouldBe ObservablesWithSubscribeUiState(
                            error = true,
                            loading = false,
                            name = ""
                        )
                    }
                }
            }
        }
    }
})
