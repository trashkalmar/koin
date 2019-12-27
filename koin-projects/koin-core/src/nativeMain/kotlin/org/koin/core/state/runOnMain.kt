package org.koin.core.state

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.staticCFunction
import platform.darwin.dispatch_async_f
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_sync
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze

internal val resultAtom = AtomicReference<Any?>(null).freeze()

internal actual fun <R> runOnMain(block: () -> R): R {
    block.freeze()

    val dispatchBlock: () -> Unit = {
        initRuntimeIfNeeded()
        val result = block().freeze()
        resultAtom.value = result
    }

    dispatch_sync(dispatch_get_main_queue(), dispatchBlock.freeze())

    val result = resultAtom.value as R
    resultAtom.value = null

    return result
}