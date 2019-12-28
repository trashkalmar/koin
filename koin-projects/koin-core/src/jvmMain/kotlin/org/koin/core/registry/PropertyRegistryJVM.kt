package org.koin.core.registry

import org.koin.core.logger.Level
import org.koin.ext.isFloat
import org.koin.ext.isInt
import org.koin.ext.quoted
import java.util.*

/**
 *Save properties values into PropertyRegister
 */
fun PropertyRegistry.saveProperties(properties: Properties) {
    if (_koin._logger.isAt(Level.DEBUG)) {
        _koin._logger.debug("load ${properties.size} properties")
    }

    val propertiesMapValues = properties.toMap() as Map<String, String>
    propertiesMapValues.forEach { (k: String, v: String) ->
        when {
            v.isInt() -> saveProperty(k, v.toInt())
            v.isFloat() -> saveProperty(k, v.toFloat())
            else -> saveProperty(k, v.quoted())
        }
    }
}