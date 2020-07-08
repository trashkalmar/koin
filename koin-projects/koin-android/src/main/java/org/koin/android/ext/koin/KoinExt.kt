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

package org.koin.android.ext.koin

import android.app.Application
import android.content.Context
import org.koin.android.logger.AndroidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.dsl.module
import java.util.*

/**
 * Koin extensions for Android
 *
 * @author Arnaud Giuliani
 */

/**
 * Setup Android Logger for Koin
 * @param level
 */
fun KoinApplication.androidLogger(
        level: Level = Level.INFO
): KoinApplication {
    koin._logger = AndroidLogger(level)
    return this
}

/**
 * Add Context instance to Koin container
 * @param androidContext - Context
 */
fun KoinApplication.androidContext(androidContext: Context): KoinApplication {
    if (koin._logger.isAt(Level.INFO)) {
        koin._logger.info("[init] declare Android Context")
    }

    if (androidContext is Application) {
        koin.loadModules(listOf(module {
            single<Application> { androidContext }
        }))
    }
    koin.loadModules(listOf(module {
        single { androidContext }
    }))

    return this
}

/**
 * Load properties file from Assets
 * @param androidContext
 * @param koinPropertyFile
 */
fun KoinApplication.androidFileProperties(
        koinPropertyFile: String = "koin.properties"
): KoinApplication {
    koin.createRootScope()
    val koinProperties = Properties()
    val androidContext = koin.get<Context>()
    try {
        val hasFile = androidContext.assets?.list("")?.contains(koinPropertyFile) ?: false
        if (hasFile) {
            try {
                androidContext.assets.open(koinPropertyFile).use { koinProperties.load(it) }
                val nb =
                        koin._propertyRegistry.saveProperties(koinProperties)
                if (koin._logger.isAt(Level.INFO)) {
                    koin._logger.info("[Android-Properties] loaded $nb properties from assets/$koinPropertyFile")
                }
            } catch (e: Exception) {
                koin._logger.error("[Android-Properties] error for binding properties : $e")
            }
        } else {
            if (koin._logger.isAt(Level.INFO)) {
                koin._logger.info("[Android-Properties] no assets/$koinPropertyFile file to load")
            }
        }
    } catch (e: Exception) {
        koin._logger.error("[Android-Properties] error while loading properties from assets/$koinPropertyFile : $e")
    }
    return this
}
