package org.koin.core.state

internal actual val platformThreading: PlatformThreading = JsPlatformThreading

internal object JsPlatformThreading : PlatformThreading {
    override fun <R> runOnMain(block: () -> R): R {
        throw UnsupportedOperationException("Jvm has no way to run something on the main thread")
    }

    override val multithreadingCapable: Boolean = false
    override val isMainThread: Boolean = true
}