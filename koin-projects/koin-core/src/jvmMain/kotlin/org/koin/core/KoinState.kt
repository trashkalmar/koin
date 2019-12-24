package org.koin.core

import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Logger
import org.koin.core.module.Module
import org.koin.core.registry.PropertyRegistry
import org.koin.core.registry.ScopeRegistry

/**
 * Koin
 *
 * Gather main features to use on Koin context
 *
 * @author Arnaud Giuliani
 */
internal actual class KoinState actual constructor(koin: Koin) {
    actual val _scopeRegistry: ScopeRegistry = ScopeRegistry(koin)
    actual val _propertyRegistry: PropertyRegistry = PropertyRegistry(koin)
    actual var _logger: Logger = EmptyLogger()
    actual val _modules: MutableSet<Module> = hashSetOf()
}