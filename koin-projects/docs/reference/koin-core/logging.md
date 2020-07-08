
Koin has a simple logging API to log any Koin activity (allocation, lookup ...). The logging API is represented by the class below:

Koin Logger

```kotlin
abstract class Logger(var level: Level = Level.INFO) {

    abstract fun log(level: Level, msg: MESSAGE)

    fun debug(msg: MESSAGE) {
        log(Level.DEBUG, msg)
    }

    fun info(msg: MESSAGE) {
        log(Level.INFO, msg)
    }

    fun error(msg: MESSAGE) {
        log(Level.ERROR, msg)
    }
}
```

Koin proposes some implementation of logging, in function of the target platform:

* `PrintLogger` - directly log into console (included in `koin-core`)
* `EmptyLogger` - log nothing (included in `koin-core`)
* `SLF4JLogger` - Log with SLF4J. Used by ktor and spark (`koin-logger-slf4j` project)
* `AndroidLogger` - log into Android Logger (included in `koin-android`)

## Set logging at start

By default, By default Koin use the `EmptyLogger`. You can use directly the `PrintLogger` as following:

```kotlin
startKoin{
    logger(LEVEL.INFO)
}
```


