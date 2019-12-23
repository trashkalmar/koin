package org.koin.test

import kotlin.test.assertNull
import org.koin.core.context.GlobalContext

fun assertHasNoStandaloneInstance() {
    assertNull(GlobalContext.getOrNull())
}