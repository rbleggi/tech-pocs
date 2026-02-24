package com.rbleggi.oauth2;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("OAuth2");
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
