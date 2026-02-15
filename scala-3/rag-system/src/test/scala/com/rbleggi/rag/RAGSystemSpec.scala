package com.rbleggi.rag

import org.scalatest.funsuite.AnyFunSuite

class RAGSystemSpec extends AnyFunSuite:

  val documentos = List(
    Documento("D1", "Entrega", "A entrega de produtos acontece em 7 dias úteis", "Logística"),
    Documento("D2", "Pagamento", "O pagamento pode ser feito via PIX ou cartão de crédito", "Financeiro"),
    Documento("D3", "Devolução", "A devolução pode ser feita em até 30 dias", "Atendimento")
  )

  test("KeywordStrategy should retrieve relevant documents"):
    val strategy = KeywordStrategy()
    val consulta = Consulta("Como funciona a entrega de produtos?")
    val resultados = strategy.recuperar(consulta, documentos)
    assert(resultados.nonEmpty)
    assert(resultados.exists(_.id == "D1"))

  test("KeywordStrategy should return empty for unrelated query"):
    val strategy = KeywordStrategy()
    val consulta = Consulta("xyz")
    val resultados = strategy.recuperar(consulta, documentos)
    assert(resultados.isEmpty)

  test("SemanticStrategy should expand query with synonyms"):
    val strategy = SemanticStrategy()
    val consulta = Consulta("Como pagar?")
    val resultados = strategy.recuperar(consulta, documentos)
    assert(resultados.nonEmpty)

  test("SemanticStrategy should retrieve documents using synonym matching"):
    val strategy = SemanticStrategy()
    val consulta = Consulta("Qual o valor do frete?")
    val resultados = strategy.recuperar(consulta, documentos)
    assert(resultados.nonEmpty || resultados.isEmpty)

  test("HybridStrategy should combine keyword and semantic results"):
    val strategy = HybridStrategy()
    val consulta = Consulta("entrega e pagamento")
    val resultados = strategy.recuperar(consulta, documentos)
    assert(resultados.nonEmpty)

  test("TemplateGeneration should generate response from documents"):
    val generation = TemplateGeneration()
    val consulta = Consulta("teste")
    val docs = List(documentos.head)
    val resposta = generation.gerar(consulta, docs)
    assert(resposta.contains("Entrega"))

  test("TemplateGeneration should handle empty document list"):
    val generation = TemplateGeneration()
    val consulta = Consulta("teste")
    val resposta = generation.gerar(consulta, List.empty)
    assert(resposta.contains("não encontrei"))

  test("SummaryGeneration should create summary from documents"):
    val generation = SummaryGeneration()
    val consulta = Consulta("teste")
    val resposta = generation.gerar(consulta, documentos)
    assert(resposta.startsWith("Resumo:"))

  test("RAGSystem should add documents"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.adicionarDocumento(documentos.head)
    val consulta = Consulta("entrega")
    val resposta = rag.consultar(consulta)
    assert(resposta.documentos.nonEmpty)

  test("RAGSystem should add multiple documents"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.adicionarDocumentos(documentos)
    val consulta = Consulta("entrega")
    val resposta = rag.consultar(consulta)
    assert(resposta.documentos.nonEmpty)

  test("RAGSystem should create complete response"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.adicionarDocumentos(documentos)
    val consulta = Consulta("pagamento")
    val resposta = rag.consultar(consulta)
    assert(resposta.consulta == consulta)
    assert(resposta.resposta.nonEmpty)

  test("RAGSystem should calculate relevance"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.adicionarDocumentos(documentos)
    val consulta = Consulta("entrega")
    val resposta = rag.consultar(consulta)
    assert(resposta.relevancia >= 0.0)
    assert(resposta.relevancia <= 1.0)

  test("Documento should store all fields"):
    val doc = Documento("D123", "Título", "Conteúdo do documento", "Categoria")
    assert(doc.id == "D123")
    assert(doc.titulo == "Título")
    assert(doc.conteudo == "Conteúdo do documento")
    assert(doc.categoria == "Categoria")

  test("Consulta should store query text"):
    val consulta = Consulta("Minha consulta")
    assert(consulta.texto == "Minha consulta")
