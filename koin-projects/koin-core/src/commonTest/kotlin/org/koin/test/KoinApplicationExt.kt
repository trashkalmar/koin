package org.koin.test

import kotlin.test.assertEquals
import org.koin.core.KoinApplication
import org.koin.core.definition.BeanDefinition
import org.koin.core.instance.InstanceFactory
import org.koin.core.scope.ScopeStorage
import kotlin.reflect.KClass

fun KoinApplication.assertDefinitionsCount(count: Int) {
    assertEquals( count, koin._koinState._scopeRegistry.size())
}

internal fun KoinApplication.getBeanDefinition(clazz: KClass<*>): BeanDefinition<*>? {
    return koin._koinState._scopeRegistry.rootScope._scopeDefinition.definitions.firstOrNull { it.primaryType == clazz }
}

internal fun ScopeStorage.getBeanDefinition(clazz: KClass<*>): BeanDefinition<*>? {
    return _scopeDefinition.definitions.firstOrNull { it.primaryType == clazz }
}

internal fun KoinApplication.getInstanceFactory(clazz: KClass<*>): InstanceFactory<*>? {
    return koin._koinState._scopeRegistry.rootScope._instanceRegistry.instances.values.firstOrNull { it.beanDefinition.primaryType == clazz }
}

internal fun ScopeStorage.getInstanceFactory(clazz: KClass<*>): InstanceFactory<*>? {
    return _instanceRegistry.instances.values.firstOrNull { it.beanDefinition.primaryType == clazz }
}
