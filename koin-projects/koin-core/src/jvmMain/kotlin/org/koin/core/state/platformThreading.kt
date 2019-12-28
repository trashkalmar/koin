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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //TODO: May want to review this but without Android this
    private val firstThread = Thread.currentThread()

    override val isMainThread: Boolean
        get() = firstThread === Thread.currentThread()
}