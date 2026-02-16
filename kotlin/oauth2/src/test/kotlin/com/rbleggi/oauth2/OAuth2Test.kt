package com.rbleggi.oauth2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OAuth2Test {
    @Test
    fun testAuthorizationCodeGrant() {
        val request = mapOf("grant_type" to "authorization_code")
        val response = OAuth2Server.processRequest(request)
        assertNotNull(response["code"])
    }

    @Test
    fun testClientCredentialsGrant() {
        val request = mapOf("grant_type" to "client_credentials")
        val response = OAuth2Server.processRequest(request)
        assertNotNull(response["access_token"])
        assertEquals("bearer", response["token_type"])
    }

    @Test
    fun testUnsupportedGrant() {
        val request = mapOf("grant_type" to "password")
        val response = OAuth2Server.processRequest(request)
        assertEquals("unsupported_grant_type", response["error"])
    }
}
