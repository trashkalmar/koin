package org.koin.core.state

import org.koin.core.isMainThread


internal expect class MainIsolatedState<T:Any>(startVal: T) {
    fun _get(): T
}

internal inline val <T:Any> MainIsolatedState<T>.value: T
    get() {
        assertMainThread()
        return _get()
    }