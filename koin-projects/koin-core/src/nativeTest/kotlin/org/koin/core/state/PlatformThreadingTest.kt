package org.koin.core.state

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformThreadingTest {
    @Test
    fun checkPlatformThreading(){
        assertTrue(platformThreading.multithreadingCapable)
        assertTrue(platformThreading.isMainThread)
    }
}