package io.spektacle.routes

import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.spektacle.models.SubjectName
import io.spektacle.plugins.authorized
import io.spektacle.repositories.SubjectNameRepository
import io.spektacle.services.KeyPairService

fun Routing.subjectNameRoutes(
    repository: SubjectNameRepository,
    keyPairService: KeyPairService
) {
    authenticate("auth-jwt", "azure-entra-oauth") {
        authorized("SubjectNames.Manage.All") {
            route("/api/subjectnames") {
                get {
                    call.respond(repository.findAll().toList())
                }
                get("/{id}") {
                    val id = call.parameters["id"]?.toLong()
                    val subjectName = id?.let { subjectNameId -> repository.findByIdOrNull(subjectNameId) }
                    subjectName?.let { call.respond(it) }
                }
                get("/keypair") {
                    call.respond(keyPairService.generate())
                }
                post {
                    repository.create(call.receive<SubjectName>())
                }
            }
        }
    }
}
