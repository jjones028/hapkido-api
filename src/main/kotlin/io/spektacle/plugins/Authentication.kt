package io.spektacle.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import java.net.URL

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            val tenantId = "366dec87-056c-4e1e-b07d-3418246c9071"
            val clientId = "aba1e669-3317-4d67-8936-db4e74bcd865"
            val jwksUri = "https://login.microsoftonline.com/$tenantId/discovery/v2.0/keys"
            val jwtIssuerUri = "https://login.microsoftonline.com/$tenantId/v2.0"

            val jwkProvider = JwkProviderBuilder(URL(jwksUri)).build()
            verifier(jwkProvider, jwtIssuerUri) {
                withIssuer(jwtIssuerUri)
                withAudience(clientId)
            }
            validate { jwtCredential ->
                if (jwtCredential.payload.issuer != null) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
