
The `koin-ktor` project is dedicated to bring dependency injection for Ktor.

## Install Koin & inject

To start Koin container, use the `installKoin()` starter function:

```kotlin
fun Application.main() {
    // Install Ktor features
    install(DefaultHeaders)
    install(CallLogging)
    install(Koin) {
        slf4jLogger()
        modules(helloAppModule)
    }

    //...
}
```

?> You can also start it from outside of Ktor, but you won't be compatible with `autoreload` feature.

KoinComponent powers are available from `Application` class:

```kotlin
fun Application.main() {
    //...

    // Lazy inject HelloService
    val service by inject<HelloService>()

    // Routing section
    routing {
        get("/hello") {
            call.respondText(service.sayHello())
        }
    }
}
```

From `Routing` class:

```kotlin
fun Application.main() {
    //...

    // Lazy inject HelloService
    val service by inject<HelloService>()

    // Routing section
    routing {
        v1()
    }
}

fun Routing.v1() {

    // Lazy inject HelloService from within a Ktor Routing Node
    val service by inject<HelloService>()

    get("/v1/hello") {
        call.respondText("[/v1/hello] " + service.sayHello())
    }
}

```


From `Route` class:

```kotlin
fun Application.main() {
    //...

    // Lazy inject HelloService
    val service by inject<HelloService>()

    // Routing section
    routing {
        v1()
    }
}

fun Routing.v1() {
    hello()
}

fun Route.hello() {

    // Lazy inject HelloService from within a Ktor Route
    val service by inject<HelloService>()

    get("/v1/bye") {
        call.respondText("[/v1/bye] " + service.sayHello())
    }
}

```


Use Koin events

see link:++../../../examples/hello-ktor++[hello-ktor] example

[source,kotlin]
----
fun Application.main() {
    // ...

    // Install Ktor features
    environment.monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin started.")
    }
    install(Koin) {
        // ...
    }
    environment.monitor.subscribe(KoinApplicationStopPreparing) {
        log.info("Koin stopping...")
    }
    environment.monitor.subscribe(KoinApplicationStopped) {
        log.info("Koin stopped.")
    }

    //...
}

----

