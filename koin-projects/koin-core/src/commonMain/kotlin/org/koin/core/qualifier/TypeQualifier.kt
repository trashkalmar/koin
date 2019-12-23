package org.koin.core.qualifier


import org.koin.core.getFullName
import kotlin.reflect.KClass

data class TypeQualifier(val type: KClass<*>) : Qualifier {
    override val value: QualifierValue = type.getFullName()
    override fun toString(): String {
        return "'$value'"
    }
}