package org.koin.test

import platform.Foundation.NSUUID

internal actual fun randomUUID(): String = NSUUID().UUIDString