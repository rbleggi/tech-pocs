package com.rbleggi.oauth2

interface GrantHandler {
    fun handle(request: Map<String, String>): Map<String, String>
}

class AuthorizationCodeHandler : GrantHandler {
    override fun handle(request: Map<String, String>): Map<String, String> {
        val code = "auth_code_123"
        return mapOf("code" to code)
    }
}

class ClientCredentialsHandler : GrantHandler {
    override fun handle(request: Map<String, String>): Map<String, String> {
        val token = "access_token_abc"
        return mapOf("access_token" to token, "token_type" to "bearer")
    }
}

object GrantHandlerFactory {
    fun getHandler(grantType: String): GrantHandler? = when (grantType) {
        "authorization_code" -> AuthorizationCodeHandler()
        "client_credentials" -> ClientCredentialsHandler()
        else -> null
    }
}

object OAuth2Server {
    fun processRequest(params: Map<String, String>): Map<String, String> {
        val grantType = params["grant_type"] ?: ""
        return GrantHandlerFactory.getHandler(grantType)
            ?.handle(params)
            ?: mapOf("error" to "unsupported_grant_type")
    }
}

fun main() {
    val authCodeRequest = mapOf("grant_type" to "authorization_code", "client_id" to "abc", "redirect_uri" to "http://localhost/cb")
    val clientCredsRequest = mapOf("grant_type" to "client_credentials", "client_id" to "abc", "client_secret" to "xyz")
    val invalidRequest = mapOf("grant_type" to "password")

    println("Authorization Code Grant Response: " + OAuth2Server.processRequest(authCodeRequest))
    println("Client Credentials Grant Response: " + OAuth2Server.processRequest(clientCredsRequest))
    println("Invalid Grant Response: " + OAuth2Server.processRequest(invalidRequest))
}
