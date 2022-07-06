package com.oliverspryn.android.rxjava.model

import com.oliverspryn.android.rxjava.data.UserProfileRepository
import com.oliverspryn.android.rxjava.di.factories.RxJavaFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class TimeoutsViewModelTest : DescribeSpec({
    describe("The TimeoutsViewModel") {
        val rxJavaFactory: RxJavaFactory = mockk()
        val userProfileRepository: UserProfileRepository = mockk()
        val viewModelState = MutableStateFlow(TimeoutsUiState())
        val intermediateStates = mutableListOf<TimeoutsUiState>()

        val ioScheduler = TestScheduler()
        val uiScheduler = TestScheduler()

        every { userProfileRepository.getUserProfileLongWait() } returns Single.just(
            UserProfileRepository.UserInfo(
                email = "john.smith@gmail.com",
                name = "John Smith",
                nickName = "Johnny",
                picture = "https://www.fillmurray.com/300/300"
            )
        ).delay(10L, TimeUnit.SECONDS)

        every { rxJavaFactory.io } returns ioScheduler
        every { rxJavaFactory.ui } returns uiScheduler

        val uut = TimeoutsViewModel(
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
                ioScheduler.advanceTimeBy(6L, TimeUnit.SECONDS)
                ioScheduler.triggerActions()
                uiScheduler.triggerActions()
                job.cancel()

                it("lands on the error state") {
                    intermediateStates.size shouldBe 1

                    intermediateStates[0] shouldBe TimeoutsUiState(
                        error = true,
                        name = ""
                    )
                }
            }
        }
    }
})
