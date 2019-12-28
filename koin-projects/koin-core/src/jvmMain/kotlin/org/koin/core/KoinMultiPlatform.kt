package org.koin.core

import org.koin.core.logger.KOIN_TAG
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import kotlin.reflect.KClass
import kotlin.jvm.kotlin as javaClassToKotlinClass

actual object KoinMultiPlatform {
    actual fun className(kClass: KClass<*>): String {
        return kClass.java.name
    }

    actual fun printStackTrace(throwable: Throwable) {
        throwable.printStackTrace()
    }

    actual fun stackTrace(): List<String> = Thread.currentThread().stackTrace.takeWhile { !it.className.contains("sun.reflect") }.map { stackTraceElement -> stackTraceElement.toString() }

    actual fun printLog(level: Level, msg: MESSAGE) {
        val printer = if (level >= Level.ERROR) System.err else System.out
        printer.println("[$level] $KOIN_TAG $msg")
    }
}

actual typealias KoinMPClass<T> = Class<T>

actual val <T : Any> KoinMPClass<T>.kotlin: KClass<T>
    get() = javaClassToKotlinClass

internal actual fun Any.ensureNeverFrozen() {
}

internal actual fun <T> T.freeze(): T = this