package io.spektacle.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.spektacle.models.KeyPair
import io.spektacle.plugins.authorized
import io.spektacle.services.KeyPairService

fun Routing.keyPairRoutes(
    service: KeyPairService
) {
    authenticate("auth-jwt") {
        authorized("SubjectNames.Manage.All") {
            route("/api/keypairs") {
                get {
                    call.respond(HttpStatusCode.OK, service.findAll().toList())
                }
                get("/{id}") {
                    val id = call.parameters["id"]?.toLong()
                    val keyPair = id?.let { keyPairId -> service.findByIdOrNull(keyPairId) }
                    keyPair?.let { call.respond(HttpStatusCode.OK, it) }
                }
                post {
                    service.create(call.receive<KeyPair>())?.let {
                        call.respond(HttpStatusCode.Created, it)
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
                post("/generate") {
                    call.respond(
                        HttpStatusCode.Created, service.create(service.generate())
                            ?: throw RuntimeException("Could not create key pair")
                    )
                }
                put {
                    call.respond(HttpStatusCode.OK, service.update(call.receive<KeyPair>()))
                }
                delete("/{id}") {
                    call.respond(HttpStatusCode.OK, service.delete(call.receive<Long>()))
                }
            }
        }
    }
}
