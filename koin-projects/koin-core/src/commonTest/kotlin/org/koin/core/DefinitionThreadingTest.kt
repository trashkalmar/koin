package org.koin.core

import org.koin.Simple
import org.koin.core.definition.ThreadScope
import org.koin.core.logger.Level
import org.koin.core.state.PlatformThreading
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.getBeanDefinition
import kotlin.test.Test
import kotlin.test.assertFails

class DefinitionThreadingTest {
    @Test
    fun `is declared as created at start`() {
        val app = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                    module {
                        single { Simple.ComponentA() }
                    }
            )
        }

        val defA = app.getBeanDefinition(Simple.ComponentA::class) ?: error("no definition found")
        assertFails {
            defA.copy(pt = SinglePlatformThreading(), threadScope = ThreadScope.Shared)
        }
    }

    class SinglePlatformThreading: PlatformThreading{
        override val multithreadingCapable: Boolean
            get() = false
        override val isMainThread: Boolean
            get() = true

        override fun <R> runOnMain(block: () -> R): R {
            throw UnsupportedOperationException()
        }
    }
}