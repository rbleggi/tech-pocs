package com.rbleggi.chatbot

import scala.collection.mutable

case class ConversationContext(
  userName: Option[String] = None,
  history: List[String] = List.empty,
  entities: Map[String, String] = Map.empty
):
  def withUserName(name: String): ConversationContext = copy(userName = Some(name))
  def addToHistory(message: String): ConversationContext = copy(history = message :: history)
  def addEntity(key: String, value: String): ConversationContext =
    copy(entities = entities + (key -> value))
