package com.rbleggi.oauth2;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        var authCodeRequest = Map.of("grant_type", "authorization_code", "client_id", "abc", "redirect_uri", "http://localhost/cb");
        var clientCredsRequest = Map.of("grant_type", "client_credentials", "client_id", "abc", "client_secret", "xyz");
        var invalidRequest = Map.of("grant_type", "password");

        System.out.println("Authorization Code Grant Response: " + OAuth2Server.processRequest(authCodeRequest));
        System.out.println("Client Credentials Grant Response: " + OAuth2Server.processRequest(clientCredsRequest));
        System.out.println("Invalid Grant Response: " + OAuth2Server.processRequest(invalidRequest));
    }
}

interface GrantHandler {
    Map<String, String> handle(Map<String, String> request);
}

class AuthorizationCodeHandler implements GrantHandler {
    @Override
    public Map<String, String> handle(Map<String, String> request) {
        var code = "auth_code_123";
        return Map.of("code", code);
    }
}

class ClientCredentialsHandler implements GrantHandler {
    @Override
    public Map<String, String> handle(Map<String, String> request) {
        var token = "access_token_abc";
        return Map.of("access_token", token, "token_type", "bearer");
    }
}

class GrantHandlerFactory {
    static GrantHandler getHandler(String grantType) {
        return switch (grantType) {
            case "authorization_code" -> new AuthorizationCodeHandler();
            case "client_credentials" -> new ClientCredentialsHandler();
            default -> null;
        };
    }
}

class OAuth2Server {
    static Map<String, String> processRequest(Map<String, String> params) {
        var grantType = params.getOrDefault("grant_type", "");
        var handler = GrantHandlerFactory.getHandler(grantType);
        return handler != null ? handler.handle(params) : Map.of("error", "unsupported_grant_type");
    }
}
