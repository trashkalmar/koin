package org.koin.core.scope

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.state.CallerThreadContext

/**
 * Get a Koin instance
 * @param clazz
 * @param qualifier
 * @param parameters
 *
 * @return instance of type T
 */
fun <T> Scope.get(
        clazz: Class<*>,
        qualifier: Qualifier? = null,
        callerThreadContext: CallerThreadContext? = null,
        parameters: ParametersDefinition? = null
): T = get(clazz.kotlin, qualifier, callerThreadContext, parameters)