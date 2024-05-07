package io.spektacle.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

class PluginConfiguration {
    var roles: Set<String> = emptySet()
}

val RoleBasedAuthorizationPlugin = createRouteScopedPlugin(
    name = "RoleBasedAuthorization",
    createConfiguration = ::PluginConfiguration
) {
    val roles = pluginConfig.roles

    pluginConfig.apply {
        on(AuthenticationChecked) { call ->
            val tokenRoles = getRolesFromToken(call)

            val authorized = roles.any { it in tokenRoles }

            if (!authorized) {
                println("User does not have any of the following roles: $roles")
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}

private fun getRolesFromToken(call: ApplicationCall): List<String> {
    return call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("roles")
        ?.asList(String::class.java)
        ?.toList() ?: emptyList()
}

fun Route.authorized(vararg hasAnyRole: String, build: Route.() -> Unit) {
    install(RoleBasedAuthorizationPlugin) { roles = hasAnyRole.toSet() }
    build()
}
