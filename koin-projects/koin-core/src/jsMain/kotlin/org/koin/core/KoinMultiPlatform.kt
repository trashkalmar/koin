package org.koin.core

import org.koin.core.logger.KOIN_TAG
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import kotlin.reflect.KClass

actual object KoinMultiPlatform {
    actual fun className(kClass: KClass<*>): String {
        return kClass.simpleName ?: "KClass@${hashCode()}"
    }

    actual fun printStackTrace(throwable: Throwable) {
        throwable.printStackTrace()
    }

    actual fun stackTrace(): List<String> = Exception().toString().split("\n")

    actual fun printLog(level: Level, msg: MESSAGE) {
        println("[$level] $KOIN_TAG $msg")
    }
}

actual data class KoinMPClass<T : Any>(val kclass: KClass<T>)

actual val <T : Any> KoinMPClass<T>.kotlin: KClass<T>
    get() = kclass

internal actual fun Any.ensureNeverFrozen() {}

internal actual fun <T> T.freeze(): T = this