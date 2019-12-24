package org.koin.core.scope

import org.koin.core.Koin
import org.koin.core.definition.ThreadScope
import org.koin.core.mainOrBlock
import org.koin.core.mainOrBust
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.jvm.JvmOverloads
import kotlin.reflect.KClass

abstract class ScopeBasedInteractor{
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
    ): Lazy<T> = mainOrBust {
        lazy {
            mainOrBlock {
                findScope().get<T>(T::class, qualifier, parameters)
            }
        }
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
    ): Lazy<T?> = mainOrBust {
        lazy {
            mainOrBlock {
                findScope().getOrNull<T>(T::class, qualifier, parameters)
            }
        }
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
        findScope().get(T::class, qualifier, parameters)
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
        findScope().getOrNull(T::class, qualifier, parameters)
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
        findScope().get(clazz, qualifier, parameters)
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
            threadScope: ThreadScope = ThreadScope.Main,
            secondaryTypes: List<KClass<*>>? = null,
            override: Boolean = false
    ) {
        mainOrBust {
            findScope().declare(instance, qualifier, threadScope, secondaryTypes, override)
        }
    }

    /**
     * Register a callback for this Scope Instance
     */
    fun registerCallback(callback: ScopeCallback) = mainOrBust {
        findScope()._callbacks += callback
    }

    /**
     * Get a all instance for given inferred class (in primary or secondary type)
     *
     * @return list of instances of type T
     */
    inline fun <reified T : Any> getAll(): List<T> = mainOrBust {
        findScope().getAll(T::class)
    }

    /**
     * Get instance of primary type P and secondary type S
     * (not for scoped instances)
     *
     * @return instance of type S
     */
    inline fun <reified S, reified P> bind(noinline parameters: ParametersDefinition? = null): S = mainOrBlock {
        bind(S::class, P::class, parameters)
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
        findScope().bind(primaryType, secondaryType, parameters)
    }

    abstract fun findScope():ScopeStorage

    abstract fun getScope(scopeID: ScopeID):ScopeRef

    abstract fun <T> getProperty(key: String, defaultValue: T): T
    abstract fun <T> getProperty(key: String): T
}