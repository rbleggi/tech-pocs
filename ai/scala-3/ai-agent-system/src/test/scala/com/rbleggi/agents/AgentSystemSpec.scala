package com.rbleggi.agents

import org.scalatest.funsuite.AnyFunSuite

class AgentSystemSpec extends AnyFunSuite:
  test("ResearcherAgent should research scala"):
    val agent = ResearcherAgent()
    val task = AgentTask("1", "Pesquisar sobre scala", "research")
    val result = agent.process(task)

    assert(result.agent == "researcher")
    assert(result.result.contains("scala"))
    assert(result.result.contains("Functional"))

  test("ResearcherAgent should research kubernetes"):
    val agent = ResearcherAgent()
    val task = AgentTask("2", "Informacoes sobre kubernetes", "research")
    val result = agent.process(task)

    assert(result.result.contains("orchestration"))

  test("ResearcherAgent should return not found"):
    val agent = ResearcherAgent()
    val task = AgentTask("3", "Pesquisar termo desconhecido xyz", "research")
    val result = agent.process(task)

    assert(result.result.contains("not found"))

  test("WriterAgent should generate document"):
    val agent = WriterAgent()
    val task = AgentTask("4", "Joao e Maria foram passear", "writing")
    val result = agent.process(task)

    assert(result.agent == "writer")
    assert(result.result.contains("Document generated"))
    assert(result.result.contains("5 words"))

  test("ReviewerAgent should approve valid text"):
    val agent = ReviewerAgent()
    val task = AgentTask("5", "Este e um texto valido.", "review")
    val result = agent.process(task)

    assert(result.agent == "reviewer")
    assert(result.result.contains("approved"))

  test("ReviewerAgent should detect short text"):
    val agent = ReviewerAgent()
    val task = AgentTask("6", "Curto", "review")
    val result = agent.process(task)

    assert(result.result.contains("too short"))

  test("ReviewerAgent should detect missing punctuation"):
    val agent = ReviewerAgent()
    val task = AgentTask("7", "Texto sem pontuacao final", "review")
    val result = agent.process(task)

    assert(result.result.contains("punctuation"))

  test("AgentSystem should execute task with researcher"):
    val system = AgentSystem()
    val task = AgentTask("8", "Pesquisar brasil", "research")
    val result = system.executeTask("researcher", task)

    assert(result.isDefined)
    assert(result.get.result.contains("brasil"))

  test("AgentSystem should execute task with writer"):
    val system = AgentSystem()
    val task = AgentTask("9", "Texto de teste", "writing")
    val result = system.executeTask("writer", task)

    assert(result.isDefined)
    assert(result.get.agent == "writer")

  test("AgentSystem should send message"):
    val system = AgentSystem()
    val message = Message("researcher", "writer", "Research data")
    system.sendMessage(message)

    val messages = system.getMessages
    assert(messages.contains(message))

  test("AgentSystem should collaborate between agents"):
    val system = AgentSystem()
    val task = AgentTask("10", "Pesquisar sobre scala", "collaboration")
    val results = system.collaborate(task)

    assert(results.length == 3)
    assert(results(0).agent == "researcher")
    assert(results(1).agent == "writer")
    assert(results(2).agent == "reviewer")
