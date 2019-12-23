package org.koin.core

import kotlin.reflect.KClass

expect class KoinMPClass<T : Any>

expect val <T : Any> KoinMPClass<T>.kotlin: KClass<T>

internal expect fun Any.ensureNeverFrozen()

internal expect fun <T> T.freeze(): T

internal expect val isMainThread:Boolean

expect object KoinMultiPlatform {
//    fun <K, V> emptyMutableMap(): MutableMap<K, V>
//
//    fun <T> emptyMutableSet(): MutableSet<T>
//
//    fun <T> emptyMutableList(): MutableList<T>

//    fun stackTrace(throwable: Throwable): List<String>

    fun nanoTime(): Long

//    fun getSystemProperties(): Map<String, String>
//
//    fun loadResourceString(fileName: String): String?

//    fun parseProperties(content: String): KoinMPProperties

    fun className(kClass: KClass<*>): String

    fun printStackTrace(throwable: Throwable)
}