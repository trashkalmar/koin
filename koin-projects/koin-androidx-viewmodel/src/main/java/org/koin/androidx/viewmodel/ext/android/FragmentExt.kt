/*
 * Copyright 2017-2020 the original author or authors.
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
package org.koin.androidx.viewmodel.ext.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.koin.getStateViewModel
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

/**
 * Fragment extension to help for Viewmodel
 *
 * @author Arnaud Giuliani
 */

inline fun <reified T : ViewModel> Fragment.sharedViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { getSharedViewModel<T>(qualifier, parameters) }

fun <T : ViewModel> Fragment.sharedViewModel(
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { getSharedViewModel(clazz, qualifier, parameters) }

inline fun <reified T : ViewModel> Fragment.getSharedViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return getSharedViewModel(T::class, qualifier, parameters)
}

fun <T : ViewModel> Fragment.getSharedViewModel(
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null
): T {
    return getKoin().getViewModel(
        requireActivity(),
        clazz,
        qualifier,
        parameters
    )
}

fun <T : ViewModel> Fragment.stateSharedViewModel(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        bundle: Bundle? = null,
        parameters: ParametersDefinition? = null
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { getStateSharedViewModel(clazz, qualifier, bundle, parameters) }
}

inline fun <reified T : ViewModel> Fragment.stateSharedViewModel(
        qualifier: Qualifier? = null,
        bundle: Bundle? = null,
        noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { getStateSharedViewModel(T::class, qualifier, bundle, parameters) }
}

inline fun <reified T : ViewModel> Fragment.getStateSharedViewModel(
        qualifier: Qualifier? = null,
        bundle: Bundle? = null,
        noinline parameters: ParametersDefinition? = null
): T {
    return getStateViewModel(T::class, qualifier, bundle, parameters)
}

fun <T : ViewModel> Fragment.getStateSharedViewModel(
        clazz: KClass<T>,
        qualifier: Qualifier? = null,
        bundle: Bundle? = null,
        parameters: ParametersDefinition? = null
): T {
    val bundleOrDefault: Bundle = bundle ?: Bundle()
    return getKoin().getStateViewModel(requireActivity(), clazz, qualifier, bundleOrDefault, parameters)
}