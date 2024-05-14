package io.spektacle.plugins

import io.ktor.server.application.Application
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
        userRoutes(UserRepository())
        subjectNameRoutes(SubjectNameRepository())
        keyPairRoutes(OpenSSLKeyPairService(KeyPairRepository()))
    }
}
