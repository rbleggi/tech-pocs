package com.rbleggi.oauth2

import org.scalatest.funsuite.AnyFunSuite

class Oauth2Spec extends AnyFunSuite {
  test("AuthorizationCodeHandler returns code") {
    val handler = new AuthorizationCodeHandler
    val result = handler.handle(Map("grant_type" -> "authorization_code"))
    assert(result("code").startsWith("auth_code_"))
  }

  test("ClientCredentialsHandler returns access_token and token_type") {
    val handler = new ClientCredentialsHandler
    val result = handler.handle(Map("grant_type" -> "client_credentials"))
    assert(result("access_token").startsWith("access_token_"))
    assert(result("token_type") == "bearer")
  }

  test("GrantHandlerFactory returns correct handler or None") {
    assert(GrantHandlerFactory.getHandler("authorization_code").exists(_.isInstanceOf[AuthorizationCodeHandler]))
    assert(GrantHandlerFactory.getHandler("client_credentials").exists(_.isInstanceOf[ClientCredentialsHandler]))
    assert(GrantHandlerFactory.getHandler("invalid").isEmpty)
  }

  test("OAuth2Server returns error for unsupported grant type") {
    val result = OAuth2Server.processRequest(Map("grant_type" -> "invalid"))
    assert(result("error") == "unsupported_grant_type")
  }
}
