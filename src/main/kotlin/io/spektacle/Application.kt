package io.spektacle

import io.ktor.server.application.*
import io.spektacle.plugins.configureDatabases
import io.spektacle.plugins.configureRouting
import io.spektacle.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
}
