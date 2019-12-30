package org.koin.core.state

internal actual val platformThreading: PlatformThreading by lazy {
    try {
        val cl = Class.forName("org.koin.android.state.AndroidPlatformThreading")
        cl.newInstance() as PlatformThreading
    } catch (e: Exception) {
        JvmPlatformThreading
    }
}

internal object JvmPlatformThreading : PlatformThreading {
    override fun <R> runOnMain(block: () -> R): R {
        throw UnsupportedOperationException("Jvm has no way to run something on the main thread")
    }

    //No way to run something on the main thread, so we can't run on background threads.
    override val multithreadingCapable: Boolean = false

    //TODO: May want to review this but without Android this
    private val firstThread = Thread.currentThread()

    override val isMainThread: Boolean
        get() = firstThread === Thread.currentThread()
}