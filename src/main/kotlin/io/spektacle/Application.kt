package io.spektacle

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.oauth
import io.ktor.server.response.respond
import io.spektacle.plugins.configureDatabases
import io.spektacle.plugins.configureRouting
import io.spektacle.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Authentication) {
        oauth("azure-entra-oauth") {
            client = HttpClient()
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "azure-oauth",
                    authorizeUrl = "https://login.microsoftonline.com/366dec87-056c-4e1e-b07d-3418246c9071/oauth2/v2.0/authorize",
                    accessTokenUrl = "https://login.microsoftonline.com/366dec87-056c-4e1e-b07d-3418246c9071/oauth2/v2.0/token",
                    clientId = "aba1e669-3317-4d67-8936-db4e74bcd865",
                    clientSecret = "",
                    accessTokenRequiresBasicAuth = false,
                    requestMethod = HttpMethod.Post,
                    defaultScopes = listOf("api://aba1e669-3317-4d67-8936-db4e74bcd865/access_via_approle_assignments"),
                )
            }
            urlProvider = {
                "http://localhost:8080/callback"
            }
        }
        jwt("auth-jwt") {
            val TENANT_ID = "366dec87-056c-4e1e-b07d-3418246c9071"
            val CLIENT_ID = "aba1e669-3317-4d67-8936-db4e74bcd865"
            val JWKS_URI = "https://login.microsoftonline.com/$TENANT_ID/discovery/v2.0/keys"
            val JWT_ISSUER_URI = "https://login.microsoftonline.com/$TENANT_ID/v2.0"

            val jwkProvider = JwkProviderBuilder(JWKS_URI).build()
            realm = ""
            verifier(jwkProvider, JWT_ISSUER_URI) {
                withIssuer(JWT_ISSUER_URI)
                withAudience(CLIENT_ID)
                acceptLeeway(10)
            }
            validate { jwtCredential ->
                if (jwtCredential.payload.issuer != null) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
    configureSerialization()
    configureDatabases()
    configureRouting()
}
