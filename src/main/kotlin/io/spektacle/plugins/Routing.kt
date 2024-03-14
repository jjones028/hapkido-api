package io.spektacle.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.spektacle.routes.userRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
    }
}
