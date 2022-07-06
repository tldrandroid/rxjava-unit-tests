package com.oliverspryn.android.rxjava.model

import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class SwitchingSchedulersViewModelTest : DescribeSpec({
    describe("The SwitchingSchedulersViewModel") {
        val rxJavaFactory: RxJavaFactory = mockk()
        val userProfileRepository: UserProfileRepository = mockk()
        val viewModelState = MutableStateFlow(SwitchingSchedulersUiState())
        val intermediateStates = mutableListOf<SwitchingSchedulersUiState>()

        val ioScheduler = TestScheduler()
        val uiScheduler = TestScheduler()

        every { userProfileRepository.getActiveLogins() } returns Single.just(
            listOf(
                UserProfileRepository.Logins(
                    deviceName = "Samsung Note 10",
                    location = "New York, NY"
                ),
                UserProfileRepository.Logins(
                    deviceName = "Samsung Note 20",
                    location = "New York, NY"
                ),
                UserProfileRepository.Logins(
                    deviceName = "Google Pixel 6 Pro",
                    location = "Hackensack, NJ"
                )
            )
        )

        every { userProfileRepository.getUserProfile() } returns Single.just(
            UserProfileRepository.UserInfo(
                email = "john.smith@gmail.com",
                name = "John Smith",
                nickName = "Johnny",
                picture = "https://www.fillmurray.com/300/300"
            )
        )

        every { rxJavaFactory.io } returns ioScheduler
        every { rxJavaFactory.ui } returns uiScheduler

        val uut = SwitchingSchedulersViewModel(
            rxJavaFactory,
            userProfileRepository,
            viewModelState
        )

        describe("when getAllUserData") {
            runTest {
                val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModelState.collect {
                        intermediateStates.add(it)
                    }
                }

                intermediateStates.clear() // Clear off value emitted by initial state

                uut.getAllUserData()
                ioScheduler.triggerActions()
                uiScheduler.triggerActions()
                ioScheduler.triggerActions()
                uiScheduler.triggerActions()

                job.cancel()

                it("updates the state with the given name, then the device list") {
                    intermediateStates.size shouldBe 2

                    intermediateStates[0] shouldBe SwitchingSchedulersUiState(
                        devices = "",
                        name = "John Smith"
                    )

                    intermediateStates[1] shouldBe SwitchingSchedulersUiState(
                        devices = "Samsung Note 10, Samsung Note 20, Google Pixel 6 Pro",
                        name = "John Smith"
                    )
                }
            }
        }
    }
})
