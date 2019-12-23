package org.koin.test

import kotlin.test.assertEquals
import org.koin.core.KoinApplication
import org.koin.core.KoinState
import org.koin.core.definition.BeanDefinition
import org.koin.core.instance.InstanceFactory
import kotlin.reflect.KClass

fun KoinApplication.assertDefinitionsCount(count: Int) {
    assertEquals( count, KoinState._scopeRegistry.size())
}

internal fun KoinApplication.getBeanDefinition(clazz: KClass<*>): BeanDefinition<*>? {
    return KoinState._scopeRegistry.rootScope._scopeDefinition.definitions.firstOrNull { it.primaryType == clazz }
}

/*internal fun Scope.getBeanDefinition(clazz: KClass<*>): BeanDefinition<*>? {
    return _scopeDefinition.definitions.firstOrNull { it.primaryType == clazz }
}*/

internal fun KoinApplication.getInstanceFactory(clazz: KClass<*>): InstanceFactory<*>? {
    return KoinState._scopeRegistry.rootScope._instanceRegistry.instances.values.firstOrNull { it.beanDefinition.primaryType == clazz }
}

/*
internal fun Scope.getInstanceFactory(clazz: KClass<*>): InstanceFactory<*>? {
    return _instanceRegistry.instances.values.firstOrNull { it.beanDefinition.primaryType == clazz }
}*/
