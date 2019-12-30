package org.koin.android.state

import android.os.Handler
import android.os.Looper
import org.koin.core.state.PlatformThreading
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

class AndroidPlatformThreading : PlatformThreading{
    private val mainHandler = Handler(Looper.getMainLooper())
    override fun <R> runOnMain(block: () -> R): R {

        val atomResult = AtomicReference<R>()
        val latch = CountDownLatch(1)

        mainHandler.post {
            atomResult.set(block())
            latch.countDown()
        }

        latch.await()
        return atomResult.get()
    }

    override val multithreadingCapable: Boolean = true

    override val isMainThread: Boolean
        get() = Looper.myLooper() === Looper.getMainLooper()
}