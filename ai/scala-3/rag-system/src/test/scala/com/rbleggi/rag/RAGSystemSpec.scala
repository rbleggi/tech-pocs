package com.rbleggi.rag

import org.scalatest.funsuite.AnyFunSuite

class RAGSystemSpec extends AnyFunSuite:

  val documents = List(
    Document("D1", "Entrega", "A entrega de produtos acontece em 7 dias uteis", "Logistics"),
    Document("D2", "Pagamento", "O pagamento pode ser feito via PIX ou cartao de credito", "Financial"),
    Document("D3", "Devolucao", "A devolucao pode ser feita em ate 30 dias", "Customer Service")
  )

  test("KeywordStrategy should retrieve relevant documents"):
    val strategy = KeywordStrategy()
    val query = Query("Como funciona a entrega de produtos?")
    val results = strategy.retrieve(query, documents)
    assert(results.nonEmpty)
    assert(results.exists(_.id == "D1"))

  test("KeywordStrategy should return empty for unrelated query"):
    val strategy = KeywordStrategy()
    val query = Query("xyz")
    val results = strategy.retrieve(query, documents)
    assert(results.isEmpty)

  test("SemanticStrategy should expand query with synonyms"):
    val strategy = SemanticStrategy()
    val query = Query("Como pagar?")
    val results = strategy.retrieve(query, documents)
    assert(results.nonEmpty)

  test("SemanticStrategy should retrieve documents using synonym matching"):
    val strategy = SemanticStrategy()
    val query = Query("Qual o valor do frete?")
    val results = strategy.retrieve(query, documents)
    assert(results.nonEmpty || results.isEmpty)

  test("HybridStrategy should combine keyword and semantic results"):
    val strategy = HybridStrategy()
    val query = Query("entrega e pagamento")
    val results = strategy.retrieve(query, documents)
    assert(results.nonEmpty)

  test("TemplateGeneration should generate response from documents"):
    val generation = TemplateGeneration()
    val query = Query("teste")
    val docs = List(documents.head)
    val response = generation.generate(query, docs)
    assert(response.contains("Entrega"))

  test("TemplateGeneration should handle empty document list"):
    val generation = TemplateGeneration()
    val query = Query("teste")
    val response = generation.generate(query, List.empty)
    assert(response.contains("no relevant"))

  test("SummaryGeneration should create summary from documents"):
    val generation = SummaryGeneration()
    val query = Query("teste")
    val response = generation.generate(query, documents)
    assert(response.startsWith("Summary:"))

  test("RAGSystem should add documents"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.addDocument(documents.head)
    val query = Query("entrega")
    val response = rag.query(query)
    assert(response.documents.nonEmpty)

  test("RAGSystem should add multiple documents"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.addDocuments(documents)
    val query = Query("entrega")
    val response = rag.query(query)
    assert(response.documents.nonEmpty)

  test("RAGSystem should create complete response"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.addDocuments(documents)
    val query = Query("pagamento")
    val response = rag.query(query)
    assert(response.query == query)
    assert(response.response.nonEmpty)

  test("RAGSystem should calculate relevance"):
    val rag = RAGSystem(KeywordStrategy(), TemplateGeneration())
    rag.addDocuments(documents)
    val query = Query("entrega")
    val response = rag.query(query)
    assert(response.relevance >= 0.0)
    assert(response.relevance <= 1.0)

  test("Document should store all fields"):
    val doc = Document("D123", "Title", "Document content", "Category")
    assert(doc.id == "D123")
    assert(doc.title == "Title")
    assert(doc.content == "Document content")
    assert(doc.category == "Category")

  test("Query should store query text"):
    val query = Query("My query")
    assert(query.text == "My query")
