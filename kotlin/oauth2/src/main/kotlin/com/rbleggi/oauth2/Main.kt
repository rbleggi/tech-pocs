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
    println("OAuth2")
}
