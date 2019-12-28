package org.koin.concurrent

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import co.touchlab.testhelp.concurrency.ThreadOperations
import org.koin.core.freeze
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.*

class ConcurrentConfigTest {

    val scopeKey = named("KEY")
    val koin = koinApplication {
        modules(
                module {
                    scope(scopeKey) {
                    }
                }
        )
    }.koin

/*    @Test
    fun `create a scope instance`() {
        val ops = ThreadOperations({})
        ops.exe {
            val scopeId = "myScope"
            val scope1 = koin.createScope(scopeId, scopeKey)
            val scope2 = koin.getScope(scopeId)

            assertEquals(scope1, scope2)
        }

        assertFails { ops.run(4) }
    }*/
}