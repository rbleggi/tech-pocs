package com.rbleggi.oauth2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OAuth2Test {

    @Test
    @DisplayName("AuthorizationCodeHandler should return authorization code")
    void authorizationCodeHandler_returnsCode() {
        var handler = new AuthorizationCodeHandler();
        var request = Map.of("grant_type", "authorization_code", "client_id", "abc");
        var response = handler.handle(request);
        assertTrue(response.containsKey("code"));
        assertEquals("auth_code_123", response.get("code"));
    }

    @Test
    @DisplayName("ClientCredentialsHandler should return access token")
    void clientCredentialsHandler_returnsToken() {
        var handler = new ClientCredentialsHandler();
        var request = Map.of("grant_type", "client_credentials", "client_id", "abc");
        var response = handler.handle(request);
        assertTrue(response.containsKey("access_token"));
        assertEquals("access_token_abc", response.get("access_token"));
        assertEquals("bearer", response.get("token_type"));
    }

    @Test
    @DisplayName("GrantHandlerFactory should return AuthorizationCodeHandler for authorization_code")
    void grantHandlerFactory_authorizationCode_returnsCorrectHandler() {
        var handler = GrantHandlerFactory.getHandler("authorization_code");
        assertInstanceOf(AuthorizationCodeHandler.class, handler);
    }

    @Test
    @DisplayName("GrantHandlerFactory should return ClientCredentialsHandler for client_credentials")
    void grantHandlerFactory_clientCredentials_returnsCorrectHandler() {
        var handler = GrantHandlerFactory.getHandler("client_credentials");
        assertInstanceOf(ClientCredentialsHandler.class, handler);
    }

    @Test
    @DisplayName("GrantHandlerFactory should return null for unsupported grant type")
    void grantHandlerFactory_unsupportedGrantType_returnsNull() {
        var handler = GrantHandlerFactory.getHandler("password");
        assertNull(handler);
    }

    @Test
    @DisplayName("OAuth2Server should process authorization code request")
    void oauth2Server_authorizationCodeRequest_returnsCode() {
        var request = Map.of("grant_type", "authorization_code", "client_id", "abc");
        var response = OAuth2Server.processRequest(request);
        assertTrue(response.containsKey("code"));
    }

    @Test
    @DisplayName("OAuth2Server should process client credentials request")
    void oauth2Server_clientCredentialsRequest_returnsToken() {
        var request = Map.of("grant_type", "client_credentials", "client_id", "abc");
        var response = OAuth2Server.processRequest(request);
        assertTrue(response.containsKey("access_token"));
    }

    @Test
    @DisplayName("OAuth2Server should return error for unsupported grant type")
    void oauth2Server_unsupportedGrantType_returnsError() {
        var request = Map.of("grant_type", "password");
        var response = OAuth2Server.processRequest(request);
        assertTrue(response.containsKey("error"));
        assertEquals("unsupported_grant_type", response.get("error"));
    }

    @Test
    @DisplayName("OAuth2Server should return error for missing grant type")
    void oauth2Server_missingGrantType_returnsError() {
        var request = Map.<String, String>of();
        var response = OAuth2Server.processRequest(request);
        assertTrue(response.containsKey("error"));
    }
}
