package org.koin.core

import kotlin.reflect.KClass
import kotlin.jvm.kotlin as javaClassToKotlinClass

actual object KoinMultiPlatform {
//    actual fun <K, V> emptyMutableMap(): MutableMap<K, V> {
//        return java.util.concurrent.ConcurrentHashMap()
//    }
//
//    actual fun stackTrace(throwable: Throwable): List<String> {
//        return throwable.stackTrace
//                .map { it.toString() }
//                .takeWhile { !it.contains("sun.reflect") }
//    }

    actual fun nanoTime(): Long {
        return System.nanoTime()
    }
//
//    actual fun getSystemProperties(): Map<String, String> {
//        @Suppress("UNCHECKED_CAST")
//        return System.getProperties().toMap()
//    }
//
//    actual fun loadResourceString(fileName: String): String? {
//        return Koin::class.java.getResource(fileName)?.readText()
//    }
//
//    actual fun parseProperties(content: String): KoinMPProperties {
//        val properties = KoinMPProperties()
//        properties.load(content.byteInputStream())
//        return properties
//    }

    actual fun className(kClass: KClass<*>): String {
        return kClass.java.name
    }

    actual fun printStackTrace(throwable: Throwable) {
        throwable.printStackTrace()
    }
//
//    actual fun <T> emptyMutableSet(): MutableSet<T> = HashSet()
//
//    actual fun <T> emptyMutableList(): MutableList<T> = arrayListOf()
}

//TODO: May want to review this but without Android this
private val firstThread = Thread.currentThread()

internal actual val isMainThread: Boolean
    get() = firstThread === Thread.currentThread()

actual typealias KoinMPClass<T> = Class<T>

actual val <T : Any> KoinMPClass<T>.kotlin: KClass<T>
    get() = javaClassToKotlinClass

internal actual fun Any.ensureNeverFrozen() {
}

internal actual fun <T> T.freeze(): T = this