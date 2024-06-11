package io.spektacle.plugins

import io.ktor.server.application.Application
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import io.spektacle.repositories.KeyPairRepository
import io.spektacle.repositories.SubjectNameRepository
import io.spektacle.repositories.UserRepository
import io.spektacle.routes.keyPairRoutes
import io.spektacle.routes.subjectNameRoutes
import io.spektacle.routes.userRoutes
import io.spektacle.services.OpenSSLKeyPairService

fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger")
        openAPI(path = "openapi")
        userRoutes(repository = UserRepository())
        subjectNameRoutes(repository = SubjectNameRepository())
        keyPairRoutes(service = OpenSSLKeyPairService(repository = KeyPairRepository()))
    }
}
