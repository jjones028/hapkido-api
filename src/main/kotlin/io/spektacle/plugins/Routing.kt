package io.spektacle.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.spektacle.routes.subjectNameRoutes
import io.spektacle.routes.userRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
        subjectNameRoutes()
    }
}
