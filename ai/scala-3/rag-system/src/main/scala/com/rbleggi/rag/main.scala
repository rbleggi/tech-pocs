package com.rbleggi.rag

case class Document(id: String, title: String, content: String, category: String)

case class Query(text: String)

case class Response(query: Query, documents: List[Document], response: String, relevance: Double)

trait RetrievalStrategy:
  def retrieve(query: Query, documents: List[Document]): List[Document]

class KeywordStrategy extends RetrievalStrategy:
  private def extractKeywords(text: String): Set[String] =
    text.toLowerCase
      .replaceAll("[^a-záàâãéèêíïóôõúüç\\s]", "")
      .split("\\s+")
      .filter(_.length > 3)
      .toSet

  private def calculateRelevance(query: Set[String], doc: Document): Int =
    val docWords = extractKeywords(doc.content)
    query.intersect(docWords).size

  def retrieve(query: Query, documents: List[Document]): List[Document] =
    val queryWords = extractKeywords(query.text)
    documents
      .map(doc => (doc, calculateRelevance(queryWords, doc)))
      .filter(_._2 > 0)
      .sortBy(-_._2)
      .take(3)
      .map(_._1)

class SemanticStrategy extends RetrievalStrategy:
  private val synonyms = Map(
    "produto" -> Set("produto", "item", "mercadoria", "artigo"),
    "preco" -> Set("preco", "valor", "custo", "tarifa"),
    "entrega" -> Set("entrega", "envio", "frete", "remessa"),
    "pagamento" -> Set("pagamento", "pagar", "transacao", "cobranca")
  )

  private def expandQuery(text: String): Set[String] =
    val words = text.toLowerCase.replaceAll("[^a-záàâãéèêíïóôõúüç\\s]", "").split("\\s+").toSet
    words.flatMap { word =>
      synonyms.values.find(_.contains(word)).getOrElse(Set(word))
    }

  private def calculateRelevance(expandedQuery: Set[String], doc: Document): Int =
    val docWords = doc.content.toLowerCase.split("\\s+").toSet
    expandedQuery.intersect(docWords).size

  def retrieve(query: Query, documents: List[Document]): List[Document] =
    val expandedQuery = expandQuery(query.text)
    documents
      .map(doc => (doc, calculateRelevance(expandedQuery, doc)))
      .filter(_._2 > 0)
      .sortBy(-_._2)
      .take(3)
      .map(_._1)

class HybridStrategy extends RetrievalStrategy:
  private val keyword = KeywordStrategy()
  private val semantic = SemanticStrategy()

  def retrieve(query: Query, documents: List[Document]): List[Document] =
    val docsKeyword = keyword.retrieve(query, documents).toSet
    val docsSemantic = semantic.retrieve(query, documents).toSet
    val allRelevant = docsKeyword.union(docsSemantic).toList
    allRelevant.take(3)

trait GenerationStrategy:
  def generate(query: Query, documents: List[Document]): String

class TemplateGeneration extends GenerationStrategy:
  def generate(query: Query, documents: List[Document]): String =
    if documents.isEmpty then
      "Sorry, no relevant information found for your query."
    else
      val info = documents.map(doc =>
        s"• ${doc.title}: ${doc.content.take(100)}..."
      ).mkString("\n")
      s"Based on the query '${query.text}', found the following information:\n\n$info"

class SummaryGeneration extends GenerationStrategy:
  def generate(query: Query, documents: List[Document]): String =
    if documents.isEmpty then
      "No documents found."
    else
      val summary = documents.map(_.content).mkString(" ")
      val words = summary.split("\\s+").take(50).mkString(" ")
      s"Summary: $words..."

class RAGSystem(retrieval: RetrievalStrategy, generation: GenerationStrategy):
  private var knowledgeBase: List[Document] = List.empty

  def addDocument(doc: Document): Unit =
    knowledgeBase = doc :: knowledgeBase

  def addDocuments(docs: List[Document]): Unit =
    knowledgeBase = docs ::: knowledgeBase

  def query(query: Query): Response =
    val relevantDocs = retrieval.retrieve(query, knowledgeBase)
    val response = generation.generate(query, relevantDocs)
    val relevance = if relevantDocs.isEmpty then 0.0 else relevantDocs.length.toDouble / 3.0
    Response(query, relevantDocs, response, relevance)

@main def run(): Unit =
  println("RAG System")
