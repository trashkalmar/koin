package org.koin.core

import platform.Foundation.NSThread
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze
import kotlin.reflect.KClass
import kotlin.system.getTimeNanos

actual object KoinMultiPlatform {
    actual fun className(kClass: KClass<*>): String {
        return kClass.qualifiedName ?: "KClass@${hashCode()}"
    }

    actual fun printStackTrace(throwable: Throwable) {
        throwable.printStackTrace()
    }
}

actual data class KoinMPClass<T : Any>(val kclass: KClass<T>)

actual val <T : Any> KoinMPClass<T>.kotlin: KClass<T>
    get() = kclass

internal actual fun Any.ensureNeverFrozen() = ensureNeverFrozen()

internal actual fun <T> T.freeze(): T = this.freeze()