package com.oliverspryn.android.rxjava

import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeTestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Provides a coroutine context for view models with a MutableStateFlow<>
 * that is bound to a viewModelScope by provide a main looper. This is
 * automatically applied to all unit tests via the TestConfiguration
 * object.
 *
 * Thanks to:
 *  - https://stackoverflow.com/a/61667162/
 *  - https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-survey#3
 *  - https://kotest.io/docs/framework/extensions/extension-examples.html
 *  - https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/index.html
 *  - https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-coroutine-dispatcher/
 *  - https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-standard-test-dispatcher.html
 */
@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
object MainLooperInterceptor : BeforeTestListener, AfterTestListener {
    private val testDispatcher = newSingleThreadContext("UI thread")

    override suspend fun beforeTest(testCase: TestCase) {
        Dispatchers.setMain(testDispatcher)
    }

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
        testDispatcher.close()
    }
}
