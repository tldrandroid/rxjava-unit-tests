package com.oliverspryn.android.rxjava.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ObservablesWithoutSubscribeViewModelTest : DescribeSpec({
    describe("The ObservablesWithoutSubscribeViewModel") {
        val uut = ObservablesWithoutSubscribeViewModel()

        describe("when getNumber") {
            var didError = false
            var didSucceed = false
            var givenNumber = 0

            uut
                .getNumber()
                .subscribe({
                    didSucceed = true
                    givenNumber = it
                }, {
                    didError = true
                })

            it("succeeded with the number 4") {
                didError shouldBe false
                didSucceed shouldBe true
                givenNumber shouldBe 4
            }

            it("sets the number on the UI state to 4") {
                uut.uiState.value.number shouldBe 4
            }
        }
    }
})
