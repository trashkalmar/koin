package org.koin.core.definition

import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.ScopeDefinition
import kotlin.reflect.KClass

object Definitions {

    inline fun <reified T> saveSingle(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        threadScope: ThreadScope
    ): BeanDefinition<T> {
        val beanDefinition = createSingle(qualifier, definition, scopeDefinition, options, threadScope)
        scopeDefinition.save(beanDefinition)
        return beanDefinition
    }

    inline fun <reified T> createSingle(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        threadScope: ThreadScope,
        secondaryTypes: List<KClass<*>> = emptyList()
    ): BeanDefinition<T> {
        return BeanDefinition(
            scopeDefinition,
            T::class,
            qualifier,
            definition,
            Kind.Single,
                threadScope,
            options = options,
            secondaryTypes = secondaryTypes
        )
    }

    internal fun createSingle(
        clazz: KClass<*>,
        qualifier: Qualifier? = null,
        definition: Definition<*>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        threadScope: ThreadScope,
        secondaryTypes: List<KClass<*>> = emptyList()
    ): BeanDefinition<*> {
        return BeanDefinition(
            scopeDefinition,
            clazz,
            qualifier,
            definition,
            Kind.Single,
            threadScope,
            options = options,
            secondaryTypes = secondaryTypes
        )
    }

    inline fun <reified T> createFactory(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        threadScope: ThreadScope,
        secondaryTypes: List<KClass<*>> = emptyList()
    ): BeanDefinition<T> {
        return BeanDefinition(
            scopeDefinition,
            T::class,
            qualifier,
            definition,
            Kind.Factory,
                threadScope,
            options = options,
            secondaryTypes = secondaryTypes
        )
    }

    inline fun <reified T> saveFactory(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        threadScope: ThreadScope
    ): BeanDefinition<T> {
        val beanDefinition = createFactory(qualifier, definition, scopeDefinition, options, threadScope)
        scopeDefinition.save(beanDefinition)
        return beanDefinition
    }
}