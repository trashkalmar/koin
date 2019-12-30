package org.koin.core.state

internal actual val platformThreading: PlatformThreading
    get() = NativePlatformThreading

internal object NativePlatformThreading: PlatformThreading{
    override fun <R> runOnMain(block: () -> R): R {
        throw UnsupportedOperationException("Jvm has no way to run something on the main thread")
    }

    //No way to run something on the main thread, so we can't run on background threads.
    override val multithreadingCapable: Boolean = false

    override val isMainThread: Boolean
        get() {
            return try {
                //Trying to access a global val on a background thread will throw an exception
                dummyData
                true
            }catch (t:Throwable){
                false
            }
        }
}

data class DummyData(val s:String)

internal val dummyData = DummyData("arst")