package io.spektacle.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.spektacle.models.User
import io.spektacle.repositories.UserRepository

fun Routing.userRoutes() {
    val repository = UserRepository()

    route("/user") {
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
            repository.insert(call.receive<User>())
        }
    }
}
