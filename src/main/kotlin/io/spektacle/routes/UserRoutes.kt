package io.spektacle.routes

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.spektacle.models.User
import io.spektacle.repositories.UserRepository

fun Routing.userRoutes(repository: UserRepository) {
    route("/api/users") {
        get("/sample") {
            call.respond(listOf(
                User(1, "jjones", "Jeremy", "Jones"),
                User(2, "theprince", "The artist formally known as Prince")
            ))
        }
        get {
            call.respond(repository.findAll().toList())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLong()
            val user = id?.let { userId -> repository.findByIdOrNull(userId) }
            user?.let { call.respond(it) }
        }
        post {
            repository.create(call.receive<User>())
        }
    }
}
