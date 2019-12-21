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

import org.koin.core.error.ScopeNotCreatedException
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.registry.PropertyRegistry
import org.koin.core.registry.ScopeRegistry
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeID
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

@ThreadLocal
object KoinState {
    fun stateInit(koin: Koin) {
        _scopeRegistry = ScopeRegistry(koin)
        _propertyRegistry = PropertyRegistry(koin)
        _modules = hashSetOf<Module>()
    }

    lateinit var _scopeRegistry: ScopeRegistry// = ScopeRegistry(this)
    lateinit var _propertyRegistry: PropertyRegistry// = PropertyRegistry(this)
    lateinit var _modules: MutableSet<Module>// = hashSetOf<Module>()
}

class Koin {

    var _logger: Logger = EmptyLogger()

    /**
     * Lazy inject a Koin instance
     * @param qualifier
     * @param scope
     * @param parameters
     *
     * @return Lazy instance of type T
     */
    @JvmOverloads
    inline fun <reified T> inject(
            qualifier: Qualifier? = null,
            noinline parameters: ParametersDefinition? = null
    ): Lazy<T> = mainOrBlock {
        KoinState._scopeRegistry.rootScope.inject(qualifier, parameters)
    }

    /**
     * Lazy inject a Koin instance if available
     * @param qualifier
     * @param scope
     * @param parameters
     *
     * @return Lazy instance of type T or null
     */
    @JvmOverloads
    inline fun <reified T> injectOrNull(
            qualifier: Qualifier? = null,
            noinline parameters: ParametersDefinition? = null
    ): Lazy<T?> = mainOrBlock {
        KoinState._scopeRegistry.rootScope.injectOrNull(qualifier, parameters)
    }

    /**
     * Get a Koin instance
     * @param qualifier
     * @param scope
     * @param parameters
     */
    @JvmOverloads
    inline fun <reified T> get(
            qualifier: Qualifier? = null,
            noinline parameters: ParametersDefinition? = null
    ): T = mainOrBlock {
        KoinState._scopeRegistry.rootScope.get(qualifier, parameters)
    }

    /**
     * Get a Koin instance if available
     * @param qualifier
     * @param scope
     * @param parameters
     *
     * @return instance of type T or null
     */
    @JvmOverloads
    inline fun <reified T> getOrNull(
            qualifier: Qualifier? = null,
            noinline parameters: ParametersDefinition? = null
    ): T? = mainOrBlock {
        KoinState._scopeRegistry.rootScope.getOrNull(qualifier, parameters)
    }

    /**
     * Get a Koin instance
     * @param clazz
     * @param qualifier
     * @param scope
     * @param parameters
     *
     * @return instance of type T
     */
    fun <T> get(
            clazz: KClass<*>,
            qualifier: Qualifier? = null,
            parameters: ParametersDefinition? = null
    ): T = mainOrBlock {
        KoinState._scopeRegistry.rootScope.get(clazz, qualifier, parameters)
    }


    /**
     * Declare a component definition from the given instance
     * This result of declaring a single definition of type T, returning the given instance
     *
     * @param instance The instance you're declaring.
     * @param qualifier Qualifier for this declaration
     * @param secondaryTypes List of secondary bound types
     * @param override Allows to override a previous declaration of the same type (default to false).
     */
    fun <T : Any> declare(
            instance: T,
            qualifier: Qualifier? = null,
            secondaryTypes: List<KClass<*>>? = null,
            override: Boolean = false
    ) {
        mainOrBlock {
            KoinState._scopeRegistry.rootScope.declare(instance, qualifier, secondaryTypes, override)
        }
    }

    /**
     * Get a all instance for given inferred class (in primary or secondary type)
     *
     * @return list of instances of type T
     */
    inline fun <reified T : Any> getAll(): List<T> = mainOrBlock {
        KoinState._scopeRegistry.rootScope.getAll()
    }

    /**
     * Get instance of primary type P and secondary type S
     * (not for scoped instances)
     *
     * @return instance of type S
     */
    inline fun <reified S, reified P> bind(noinline parameters: ParametersDefinition? = null): S = mainOrBlock {
        KoinState._scopeRegistry.rootScope.bind<S, P>(parameters)
    }


    /**
     * Get instance of primary type P and secondary type S
     * (not for scoped instances)
     *
     * @return instance of type S
     */
    fun <S> bind(
            primaryType: KClass<*>,
            secondaryType: KClass<*>,
            parameters: ParametersDefinition? = null
    ): S = mainOrBlock {
        KoinState._scopeRegistry.rootScope.bind(primaryType, secondaryType, parameters)
    }

    internal fun createEagerInstances() {
        createContextIfNeeded()
        mainOrBlock {
            KoinState._scopeRegistry.rootScope.createEagerInstances()
        }
    }

    internal fun createContextIfNeeded() {
        mainOrBlock {
            if (KoinState._scopeRegistry._rootScope == null) {
                KoinState._scopeRegistry.createRootScope()
            }
        }
    }

    /**
     * Create a Scope instance
     * @param scopeId
     * @param scopeDefinitionName
     */
    fun createScope(scopeId: ScopeID, qualifier: Qualifier): Scope {
        if (_logger.isAt(Level.DEBUG)) {
            _logger.debug("!- create scope - id:'$scopeId' q:$qualifier")
        }
        assertMainThread()
        return KoinState._scopeRegistry.createScope(scopeId, qualifier)
    }

    /**
     * Get or Create a Scope instance
     * @param scopeId
     * @param qualifier
     */
    fun getOrCreateScope(scopeId: ScopeID, qualifier: Qualifier): Scope {
        assertMainThread()
        return KoinState._scopeRegistry.getScopeOrNull(scopeId) ?: createScope(scopeId, qualifier)
    }

    /**
     * get a scope instance
     * @param scopeId
     */
    fun getScope(scopeId: ScopeID): Scope {
        assertMainThread()
        return KoinState._scopeRegistry.getScopeOrNull(scopeId)
                ?: throw ScopeNotCreatedException("No scope found for id '$scopeId'")
    }

    /**
     * get a scope instance
     * @param scopeId
     */
    fun getScopeOrNull(scopeId: ScopeID): Scope? {
        assertMainThread()
        return KoinState._scopeRegistry.getScopeOrNull(scopeId)
    }

    /**
     * Delete a scope instance
     */
    fun deleteScope(scopeId: ScopeID) {
        assertMainThread()
        KoinState._scopeRegistry.deleteScope(scopeId)
    }

    /**
     * Retrieve a property
     * @param key
     * @param defaultValue
     */
    fun <T> getProperty(key: String, defaultValue: T): T {
        return mainOrBlock { KoinState._propertyRegistry.getProperty<T>(key) ?: defaultValue }
    }

    /**
     * Retrieve a property
     * @param key
     */
    fun <T> getProperty(key: String): T? {
        return mainOrBlock { KoinState._propertyRegistry.getProperty(key) }
    }

    /**
     * Save a property
     * @param key
     * @param value
     */
    fun <T : Any> setProperty(key: String, value: T) {
        _propertyRegistry.saveProperty(key, value)
    }

    /**
     * Close all resources from context
     */
    fun close() {
        assertMainThread()
        KoinState._modules.forEach { it.isLoaded = false }
        KoinState._modules.clear()
        KoinState._scopeRegistry.close()
        KoinState._propertyRegistry.close()
    }

    fun loadModules(modules: List<Module>) {
        assertMainThread()
        KoinState._modules.addAll(modules)
        KoinState._scopeRegistry.loadModules(modules)
    }


    fun unloadModules(modules: List<Module>) {
        assertMainThread()
        KoinState._scopeRegistry.unloadModules(modules)
        KoinState._modules.removeAll(modules)
    }

    fun createRootScope() {
        mainOrBlock {
            KoinState._scopeRegistry.createRootScope()
        }
    }
}

internal fun assertMainThread() {
    if (!isMainThread)
        throw IllegalStateException("Must be main thread")
}

fun <R> mainOrBlock(block: () -> R): R {
    return if (isMainThread) {
        block()
    } else {
        TODO()
    }
}