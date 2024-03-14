package io.spektacle.plugins

import io.ktor.server.application.*
import io.spektacle.singletons.DatabaseSingleton

fun Application.configureDatabases() {
    DatabaseSingleton.init(environment.config)
}