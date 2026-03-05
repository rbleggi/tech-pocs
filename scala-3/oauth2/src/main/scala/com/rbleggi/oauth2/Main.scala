package com.rbleggi.oauth2

trait GrantHandler {
  def handle(request: Map[String, String]): Map[String, String]
}

class AuthorizationCodeHandler extends GrantHandler {
  def handle(request: Map[String, String]): Map[String, String] = {
    val code = "auth_code_123"
    Map("code" -> code)
  }
}

class ClientCredentialsHandler extends GrantHandler {
  def handle(request: Map[String, String]): Map[String, String] = {
    val token = "access_token_abc"
    Map("access_token" -> token, "token_type" -> "bearer")
  }
}

object GrantHandlerFactory {
  def getHandler(grantType: String): Option[GrantHandler] = grantType match {
    case "authorization_code" => Some(new AuthorizationCodeHandler)
    case "client_credentials" => Some(new ClientCredentialsHandler)
    case _ => None
  }
}

object OAuth2Server {
  def processRequest(params: Map[String, String]): Map[String, String] = {
    val grantType = params.getOrElse("grant_type", "")
    GrantHandlerFactory.getHandler(grantType)
      .map(_.handle(params))
      .getOrElse(Map("error" -> "unsupported_grant_type"))
  }
}

@main def run(): Unit =
  println("OAuth2")
