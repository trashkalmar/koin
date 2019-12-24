package org.koin.core

import kotlinx.cinterop.StableRef
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Logger
import org.koin.core.module.Module
import org.koin.core.registry.PropertyRegistry
import org.koin.core.registry.ScopeRegistry
import kotlin.native.concurrent.ensureNeverFrozen

/**
 * Koin
 *
 * Gather main features to use on Koin context
 *
 * @author Arnaud Giuliani
 */
internal actual class KoinState actual constructor(koin: Koin) {
    private val stateRef: StableRef<IsolatedKoinState>
    init {
        assertMainThread()
        stateRef = StableRef.create(IsolatedKoinState(koin))
    }

    fun clear() = mainOrBust {
        stateRef.dispose()
    }

    actual val _scopeRegistry: ScopeRegistry
        get() = mainOrBust {
            stateRef.get()._scopeRegistry
        }

    actual val _propertyRegistry: PropertyRegistry
        get() = mainOrBust {
            stateRef.get()._propertyRegistry
        }

    actual var _logger: Logger
        get() = mainOrBust {
            stateRef.get()._logger
        }

        set(value) {
            mainOrBust {
                stateRef.get()._logger = value
            }
        }

    actual val _modules: MutableSet<Module>
        get() = mainOrBust {
            stateRef.get()._modules
        }
}

private class IsolatedKoinState(koin: Koin) {
    init {
        ensureNeverFrozen()
    }
    val _scopeRegistry: ScopeRegistry = ScopeRegistry(koin)
    val _propertyRegistry: PropertyRegistry = PropertyRegistry(koin)
    var _logger: Logger = EmptyLogger()
    val _modules: MutableSet<Module> = hashSetOf()
}