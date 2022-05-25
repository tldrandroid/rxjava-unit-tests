package com.oliverspryn.android.rxjava

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
object TestConfiguration : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(MainLooperInterceptor)
}
