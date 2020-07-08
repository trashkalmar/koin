package org.koin.core.definition

import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.ScopeDefinition
import kotlin.reflect.KClass

object Definitions {

    inline fun <reified T> saveSingle(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options
    ): BeanDefinition<T> {
        val beanDefinition = createSingle(qualifier, definition, scopeDefinition, options)
        scopeDefinition.save(beanDefinition)
        return beanDefinition
    }

    inline fun <reified T> createSingle(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        secondaryTypes: List<KClass<*>> = emptyList()
    ): BeanDefinition<T> {
        return BeanDefinition(
            scopeDefinition,
            T::class,
            qualifier,
            definition,
            Kind.Single,
            options = options,
            secondaryTypes = secondaryTypes
        )
    }

    fun createSingle(
        clazz: KClass<*>,
        qualifier: Qualifier? = null,
        definition: Definition<*>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        secondaryTypes: List<KClass<*>> = emptyList()
    ): BeanDefinition<*> {
        return BeanDefinition(
            scopeDefinition,
            clazz,
            qualifier,
            definition,
            Kind.Single,
            options = options,
            secondaryTypes = secondaryTypes
        )
    }

    inline fun <reified T> createFactory(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options,
        secondaryTypes: List<KClass<*>> = emptyList()
    ): BeanDefinition<T> {
        return BeanDefinition(
            scopeDefinition,
            T::class,
            qualifier,
            definition,
            Kind.Factory,
            options = options,
            secondaryTypes = secondaryTypes
        )
    }

    inline fun <reified T> saveFactory(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
        scopeDefinition: ScopeDefinition,
        options: Options
    ): BeanDefinition<T> {
        val beanDefinition = createFactory(qualifier, definition, scopeDefinition, options)
        scopeDefinition.save(beanDefinition)
        return beanDefinition
    }
}