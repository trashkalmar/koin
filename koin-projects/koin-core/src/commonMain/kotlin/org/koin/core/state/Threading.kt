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
    Main, Other, None
}

internal var currentCallerThreadContext: CallerThreadContext = CallerThreadContext.None
internal fun updateCallerThreadContext(fromMainThread:Boolean): Boolean {
    return if (currentCallerThreadContext == CallerThreadContext.None) {
        currentCallerThreadContext = if (fromMainThread) {
            CallerThreadContext.Main
        } else {
            CallerThreadContext.Other
        }
        true
    } else {
        false
    }
}

internal fun clearCallerThreadContext() {
    currentCallerThreadContext = CallerThreadContext.None
}

fun <R> mainOrBlock(block: () -> R): R {
    return if (platformThreading.isMainThread) {
        callBlock(block, true)
    } else {
        platformThreading.runOnMain {
            callBlock(block, false)
        }
    }
}

private fun <R> callBlock(block: () -> R, fromMainThread:Boolean): R {
    val updatedContext = updateCallerThreadContext(fromMainThread)
    return try {
        block()
    } finally {
        if (updatedContext)
            clearCallerThreadContext()
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