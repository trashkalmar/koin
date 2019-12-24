/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.koin.core

import org.koin.core.definition.ThreadScope
import org.koin.core.error.MissingPropertyException
import org.koin.core.error.ScopeNotCreatedException
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.registry.PropertyRegistry
import org.koin.core.registry.ScopeRegistry
import org.koin.core.scope.ScopeBasedInteractor
import org.koin.core.scope.ScopeID
import org.koin.core.scope.ScopeRef
import org.koin.core.scope.ScopeStorage
import kotlin.jvm.JvmOverloads
import kotlin.native.concurrent.ThreadLocal
import kotlin.reflect.KClass

/**
 * Koin
 *
 * Gather main features to use on Koin context
 *
 * @author Arnaud Giuliani
 */
internal expect class KoinState(koin:Koin) {
    val _scopeRegistry: ScopeRegistry
    val _propertyRegistry: PropertyRegistry
    var _logger: Logger
    val _modules :MutableSet<Module>
}

class Koin: ScopeBasedInteractor() {

    var _logger: Logger = EmptyLogger()
    internal val _koinState = KoinState(this)

    internal fun createEagerInstances() {
        createContextIfNeeded()
        mainOrBust {
            rootScopeStorage().createEagerInstances()
        }
    }

    fun rootScopeStorage() = _koinState._scopeRegistry.rootScope

    internal fun createContextIfNeeded() {
        mainOrBust {
            val _scopeRegistry = _koinState._scopeRegistry
            if (_scopeRegistry._rootScope == null) {
                _scopeRegistry.createRootScope()
            }
        }
    }

    /**
     * Create a Scope instance
     * @param scopeId
     * @param scopeDefinitionName
     */
    fun createScope(scopeId: ScopeID, qualifier: Qualifier): ScopeRef {
        if (_logger.isAt(Level.DEBUG)) {
            _logger.debug("!- create scope - id:'$scopeId' q:$qualifier")
        }
        return mainOrBust {
            _koinState._scopeRegistry.createScope(scopeId, qualifier).ref
        }
    }

    /**
     * Get or Create a Scope instance
     * @param scopeId
     * @param qualifier
     */
    fun getOrCreateScope(scopeId: ScopeID, qualifier: Qualifier): ScopeRef {
        return mainOrBust {
            _koinState._scopeRegistry.getScopeOrNull(scopeId)?.ref ?: createScope(scopeId, qualifier)
        }
    }

    /**
     * get a scope instance. The ScopeRef could just be creted, but we want to check that
     * the scope currently exists.
     * @param scopeId
     */
    override fun getScope(scopeId: ScopeID): ScopeRef {
        return mainOrBust {
            _koinState._scopeRegistry.getScopeOrNull(scopeId)?.ref
                    ?: throw ScopeNotCreatedException("No scope found for id '$scopeId'")
        }
    }

    /**
     * get a scope instance
     * @param scopeId
     */
    fun getScopeOrNull(scopeId: ScopeID): ScopeRef? {
        return mainOrBust {
            _koinState._scopeRegistry.getScopeOrNull(scopeId)?.ref
        }
    }

    /**
     * Delete a scope instance
     */
    fun deleteScope(scopeId: ScopeID) {
        mainOrBust {
            _koinState._scopeRegistry.deleteScope(scopeId)
        }
    }

    /**
     * Retrieve a property
     * @param key
     * @param defaultValue
     */
    override fun <T> getProperty(key: String, defaultValue: T): T {
        return mainOrBlock { _koinState._propertyRegistry.getProperty<T>(key) ?: defaultValue }
    }

    /**
     * Retrieve a property
     * @param key
     */
    override fun <T> getProperty(key: String): T {
        return mainOrBlock {
            _koinState._propertyRegistry.getProperty(key)?:throw MissingPropertyException("Property '$key' not found") }
    }

    /**
     * Save a property
     * @param key
     * @param value
     */
    fun <T : Any> setProperty(key: String, value: T) {
        mainOrBlock { _koinState._propertyRegistry.saveProperty(key, value) }
    }

    /**
     * Close all resources from context
     */
    fun close() {
        mainOrBust {
            _koinState._modules.forEach { it.isLoaded = false }
            _koinState._modules.clear()
            _koinState._scopeRegistry.close()
            _koinState._propertyRegistry.close()
        }
    }

    fun loadModules(modules: List<Module>) {
        mainOrBust {
            _koinState._modules.addAll(modules)
            _koinState._scopeRegistry.loadModules(modules)
        }
    }


    fun unloadModules(modules: List<Module>) {
        mainOrBust {
            _koinState._scopeRegistry.unloadModules(modules)
            _koinState._modules.removeAll(modules)
        }
    }

    fun createRootScope() {
        mainOrBust {
            _koinState._scopeRegistry.createRootScope()
        }
    }

    internal fun createRootScopeDefinition() {
        mainOrBust {
            _koinState._scopeRegistry.createRootScopeDefinition()
        }
    }

    internal fun scopeRegistrySize(): Int = mainOrBlock {
        _koinState._scopeRegistry.size()
    }

    internal fun saveProperties(values: Map<String, Any>) = mainOrBlock {
        _koinState._propertyRegistry.saveProperties(values)
    }

    internal fun unloadModules(module: Module) {
        unloadModules(listOf(module))
    }

    override fun findScope(): ScopeStorage = rootScopeStorage()
}

internal fun assertMainThread() {
    if (!isMainThread)
        throw IllegalStateException("Must be main thread")
}

fun <R> mainOrBust(block: () -> R): R {
    assertMainThread()
    return block()
}

enum class CallerThreadContext {
    Main, Other
}

fun <R> mainOrBlock(block: (CallerThreadContext) -> R): R {
    return if (isMainThread) {
        block(CallerThreadContext.Main)
    } else {
        TODO()
    }
}

/**
 * Give full class qualifier
 */
private var _classNames: HashMap<KClass<*>, String> = hashMapOf()
internal fun KClass<*>.getFullName(): String = mainOrBust {
    _classNames.getOrPut(this) { KoinMultiPlatform.className(this) }
}