package org.koin.core.state

import org.koin.core.KoinMultiPlatform
import org.koin.core.isMainThread
import kotlin.reflect.KClass


internal fun assertMainThread() {
    if (!isMainThread)
        throw IllegalStateException("Must be main thread")
}

fun <R> mainOrBust(block: () -> R): R {
    assertMainThread()
    return block()
}

enum class CallerThreadContext {
    Main, Other
}

fun <R> mainOrBlock(context: CallerThreadContext? = null, block: (CallerThreadContext) -> R): R {
    return if (isMainThread) {
        block(context ?: CallerThreadContext.Main)
    } else {
        runOnMain { block(CallerThreadContext.Other) }
    }
}

internal expect fun <R> runOnMain(block: () -> R): R

/**
 * Give full class qualifier
 */
private var _classNames: HashMap<KClass<*>, String> = hashMapOf()

fun KClass<*>.getFullName(): String = mainOrBust {
    _classNames.getOrPut(this) { KoinMultiPlatform.className(this) }
}