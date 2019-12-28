package org.koin.core

import kotlin.reflect.KClass
import kotlin.jvm.kotlin as javaClassToKotlinClass

actual object KoinMultiPlatform {
    actual fun className(kClass: KClass<*>): String {
        return kClass.java.name
    }

    actual fun printStackTrace(throwable: Throwable) {
        throwable.printStackTrace()
    }
}

actual typealias KoinMPClass<T> = Class<T>

actual val <T : Any> KoinMPClass<T>.kotlin: KClass<T>
    get() = javaClassToKotlinClass

internal actual fun Any.ensureNeverFrozen() {
}

internal actual fun <T> T.freeze(): T = this