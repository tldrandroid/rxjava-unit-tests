package com.oliverspryn.android.rxjava.model

import com.oliverspryn.android.rxjava.data.UserProfileRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow

class ObservablesWithSubscribeViewModelTest : DescribeSpec({
    describe("The ObservablesWithSubscribeViewModel") {
        val userProfileRepository: UserProfileRepository = mockk()
        val viewModelState: MutableStateFlow<ObservablesWithSubscribeUiState> =
            spyk(MutableStateFlow(ObservablesWithSubscribeUiState()))

        every { userProfileRepository.getUserProfile() } returns Single.just(
            UserProfileRepository.UserInfo(
                email = "john.smith@gmail.com",
                name = "John Smith",
                nickName = "Johnny",
                picture = "https://www.fillmurray.com/300/300"
            )
        )

        val uut = ObservablesWithSubscribeViewModel(
            userProfileRepository = userProfileRepository,
            viewModelState = viewModelState
        )

        describe("when getProfile") {
            describe("and the call succeeds") {
                uut.getProfile()

                it("updates the state to loading, then success with the user's name") {
                    verifyOrder {
                        viewModelState.compareAndSet(
                            any(), ObservablesWithSubscribeUiState(
                                error = false,
                                loading = true
                            )
                        )

                        viewModelState.compareAndSet(
                            any(), ObservablesWithSubscribeUiState(
                                error = false,
                                loading = false,
                                name = "John Smith"
                            )
                        )
                    }
                }

                it("the success state is the final state") {
                    uut.uiState.value.error shouldBe false
                    uut.uiState.value.loading shouldBe false
                    uut.uiState.value.name shouldBe "John Smith"
                }
            }

            describe("and the call does not succeed") {
                every { userProfileRepository.getUserProfile() } returns Single.error(
                    IllegalStateException()
                )

                uut.getProfile()

                it("updates the state to loading, then error") {
                    verifyOrder {
                        viewModelState.compareAndSet(
                            any(), ObservablesWithSubscribeUiState(
                                error = false,
                                loading = true
                            )
                        )

//                        viewModelState.compareAndSet(
//                            any(), ObservablesWithSubscribeUiState(
//                                error = true,
//                                loading = false
//                            )
//                        )
                    }
                }

                it("the error state is the final state") {
                    uut.uiState.value.error shouldBe true
                    uut.uiState.value.loading shouldBe false
                }
            }
        }
    }
})
