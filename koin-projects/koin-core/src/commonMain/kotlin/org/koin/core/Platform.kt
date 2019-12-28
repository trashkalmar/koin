package org.koin.core

import kotlin.reflect.KClass

expect class KoinMPClass<T : Any>

expect val <T : Any> KoinMPClass<T>.kotlin: KClass<T>

internal expect fun Any.ensureNeverFrozen()

internal expect fun <T> T.freeze(): T

expect object KoinMultiPlatform {
    fun className(kClass: KClass<*>): String
    fun printStackTrace(throwable: Throwable)
}

internal fun Throwable.printStackTrace() {
    KoinMultiPlatform.printStackTrace(this)
}