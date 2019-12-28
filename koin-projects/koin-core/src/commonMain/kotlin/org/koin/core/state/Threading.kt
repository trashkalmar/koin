package org.koin.core.state

import org.koin.core.KoinMultiPlatform
import kotlin.reflect.KClass

internal expect val platformThreading:PlatformThreading

interface PlatformThreading {
    val isMainThread: Boolean
    fun <R> runOnMain(block: () -> R): R
}

internal fun assertMainThread() {
    if (!platformThreading.isMainThread)
        throw IllegalStateException("Must be main thread")
}

enum class CallerThreadContext {
    Main, Other
}

fun <R> mainOrBlock(context: CallerThreadContext? = null, block: (CallerThreadContext) -> R): R {
    return if (platformThreading.isMainThread) {
        block(context ?: CallerThreadContext.Main)
    } else {
        platformThreading.runOnMain { block(CallerThreadContext.Other) }
    }
}

/**
 * Give full class qualifier
 */
private val _classNames: HashMap<KClass<*>, String> = hashMapOf()

fun KClass<*>.getFullName(): String {
    assertMainThread()
    return _classNames.getOrPut(this) { KoinMultiPlatform.className(this) }
}