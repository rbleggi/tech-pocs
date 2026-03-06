package com.rbleggi.domainllm

import org.scalatest.funsuite.AnyFunSuite

class DomainLLMSpec extends AnyFunSuite:
  test("MedicalDomain should respond about fever"):
    val domain = MedicalDomain()
    val context = Context("medico", "O que e febre?")
    val response = domain.generate(context)

    assert(response.response.contains("temperature"))
    assert(response.confidence > 0.9)
    assert(response.terms.contains("febre"))

  test("MedicalDomain should respond about diabetes"):
    val domain = MedicalDomain()
    val context = Context("medico", "Explique diabetes")
    val response = domain.generate(context)

    assert(response.response.contains("glucose"))
    assert(response.terms.contains("diabetes"))

  test("MedicalDomain should return not found"):
    val domain = MedicalDomain()
    val context = Context("medico", "O que e xyz?")
    val response = domain.generate(context)

    assert(response.response.contains("not found"))
    assert(response.confidence < 0.5)

  test("LegalDomain should respond about contract"):
    val domain = LegalDomain()
    val context = Context("juridico", "O que e um contrato?")
    val response = domain.generate(context)

    assert(response.response.toLowerCase.contains("agreement"))
    assert(response.confidence > 0.9)
    assert(response.terms.contains("contrato"))

  test("LegalDomain should respond about habeas corpus"):
    val domain = LegalDomain()
    val context = Context("juridico", "Explique habeas corpus")
    val response = domain.generate(context)

    assert(response.response.contains("freedom"))
    assert(response.terms.contains("habeas corpus"))

  test("LegalDomain should return not found"):
    val domain = LegalDomain()
    val context = Context("juridico", "termo desconhecido")
    val response = domain.generate(context)

    assert(response.response.contains("not found"))

  test("TechDomain should respond about API"):
    val domain = TechDomain()
    val context = Context("tecnologia", "O que e API?")
    val response = domain.generate(context)

    assert(response.response.contains("Interface"))
    assert(response.confidence > 0.9)
    assert(response.terms.contains("api"))

  test("TechDomain should respond about Scala"):
    val domain = TechDomain()
    val context = Context("tecnologia", "Explique scala")
    val response = domain.generate(context)

    assert(response.response.contains("JVM"))
    assert(response.terms.contains("scala"))

  test("DomainLLM should query medical domain"):
    val llm = DomainLLM()
    val context = Context("medico", "O que e gripe?")
    val response = llm.query(context)

    assert(response.response.toLowerCase.contains("viral"))

  test("DomainLLM should query legal domain"):
    val llm = DomainLLM()
    val context = Context("juridico", "O que e processo?")
    val response = llm.query(context)

    assert(response.response.toLowerCase.contains("judicial"))

  test("DomainLLM should return error for unknown domain"):
    val llm = DomainLLM()
    val context = Context("desconhecido", "pergunta")
    val response = llm.query(context)

    assert(response.response.contains("not supported"))
    assert(response.confidence == 0.0)
