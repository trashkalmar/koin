package org.koin.core

import platform.Foundation.NSThread
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze
import kotlin.reflect.KClass
import kotlin.system.getTimeNanos

actual object KoinMultiPlatform {
//    actual fun <K, V> emptyMutableMap(): MutableMap<K, V> = frozenHashMap()
//
//    actual fun stackTrace(throwable: Throwable): List<String> {
//        return throwable.getStackTrace().toList()
//    }

    actual fun nanoTime(): Long {
        return getTimeNanos()
    }
//
//    actual fun getSystemProperties(): Map<String, String> {
//        return emptyMap()
//    }

//    actual fun loadResourceString(fileName: String): String? {
//        // TODO
//        println("TODO: KoinMultiPlatform.loadResourceString is not yet implemented")
//        return null
//    }
//
//    actual fun parseProperties(content: String): KoinMPProperties {
//        // TODO
//        println("TODO: KoinMultiPlatform.parseProperties is not yet implemented")
//        return KoinMPProperties(emptyMap())
//    }

    actual fun className(kClass: KClass<*>): String {
        return kClass.qualifiedName ?: "KClass@${hashCode()}"
    }

    actual fun printStackTrace(throwable: Throwable) {
//        stackTrace(throwable).forEach(::println)
        throwable.printStackTrace()
    }
//
//    actual fun <T> emptyMutableSet(): MutableSet<T> = frozenHashSet()
//
//    actual fun <T> emptyMutableList(): MutableList<T> = frozenLinkedList()
}

actual data class KoinMPClass<T : Any>(val kclass: KClass<T>)

actual val <T : Any> KoinMPClass<T>.kotlin: KClass<T>
    get() = kclass

internal actual fun Any.ensureNeverFrozen() = ensureNeverFrozen()
internal actual val isMainThread: Boolean
    get() = NSThread.isMainThread

internal actual fun <T> T.freeze(): T = this.freeze()