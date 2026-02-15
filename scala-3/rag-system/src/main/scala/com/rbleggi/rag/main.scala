package com.rbleggi.rag

case class Documento(id: String, titulo: String, conteudo: String, categoria: String)

case class Consulta(texto: String)

case class Resposta(consulta: Consulta, documentos: List[Documento], resposta: String, relevancia: Double)

trait RetrievalStrategy:
  def recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento]

class KeywordStrategy extends RetrievalStrategy:
  private def extrairPalavrasChave(texto: String): Set[String] =
    texto.toLowerCase
      .replaceAll("[^a-záàâãéèêíïóôõúüç\\s]", "")
      .split("\\s+")
      .filter(_.length > 3)
      .toSet

  private def calcularRelevancia(consulta: Set[String], doc: Documento): Int =
    val palavrasDoc = extrairPalavrasChave(doc.conteudo)
    consulta.intersect(palavrasDoc).size

  def recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento] =
    val palavrasConsulta = extrairPalavrasChave(consulta.texto)
    documentos
      .map(doc => (doc, calcularRelevancia(palavrasConsulta, doc)))
      .filter(_._2 > 0)
      .sortBy(-_._2)
      .take(3)
      .map(_._1)

class SemanticStrategy extends RetrievalStrategy:
  private val sinonimos = Map(
    "produto" -> Set("produto", "item", "mercadoria", "artigo"),
    "preco" -> Set("preco", "valor", "custo", "tarifa"),
    "entrega" -> Set("entrega", "envio", "frete", "remessa"),
    "pagamento" -> Set("pagamento", "pagar", "transacao", "cobranca")
  )

  private def expandirConsulta(texto: String): Set[String] =
    val palavras = texto.toLowerCase.replaceAll("[^a-záàâãéèêíïóôõúüç\\s]", "").split("\\s+").toSet
    palavras.flatMap { palavra =>
      sinonimos.values.find(_.contains(palavra)).getOrElse(Set(palavra))
    }

  private def calcularRelevancia(consultaExpandida: Set[String], doc: Documento): Int =
    val palavrasDoc = doc.conteudo.toLowerCase.split("\\s+").toSet
    consultaExpandida.intersect(palavrasDoc).size

  def recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento] =
    val consultaExpandida = expandirConsulta(consulta.texto)
    documentos
      .map(doc => (doc, calcularRelevancia(consultaExpandida, doc)))
      .filter(_._2 > 0)
      .sortBy(-_._2)
      .take(3)
      .map(_._1)

class HybridStrategy extends RetrievalStrategy:
  private val keyword = KeywordStrategy()
  private val semantic = SemanticStrategy()

  def recuperar(consulta: Consulta, documentos: List[Documento]): List[Documento] =
    val docsKeyword = keyword.recuperar(consulta, documentos).toSet
    val docsSemantic = semantic.recuperar(consulta, documentos).toSet
    val todosRelevantes = docsKeyword.union(docsSemantic).toList
    todosRelevantes.take(3)

trait GenerationStrategy:
  def gerar(consulta: Consulta, documentos: List[Documento]): String

class TemplateGeneration extends GenerationStrategy:
  def gerar(consulta: Consulta, documentos: List[Documento]): String =
    if documentos.isEmpty then
      "Desculpe, não encontrei informações relevantes para sua consulta."
    else
      val info = documentos.map(doc =>
        s"• ${doc.titulo}: ${doc.conteudo.take(100)}..."
      ).mkString("\n")
      s"Com base na consulta '${consulta.texto}', encontrei as seguintes informações:\n\n$info"

class SummaryGeneration extends GenerationStrategy:
  def gerar(consulta: Consulta, documentos: List[Documento]): String =
    if documentos.isEmpty then
      "Nenhum documento encontrado."
    else
      val resumo = documentos.map(_.conteudo).mkString(" ")
      val palavras = resumo.split("\\s+").take(50).mkString(" ")
      s"Resumo: $palavras..."

class RAGSystem(retrieval: RetrievalStrategy, generation: GenerationStrategy):
  private var baseConhecimento: List[Documento] = List.empty

  def adicionarDocumento(doc: Documento): Unit =
    baseConhecimento = doc :: baseConhecimento

  def adicionarDocumentos(docs: List[Documento]): Unit =
    baseConhecimento = docs ::: baseConhecimento

  def consultar(consulta: Consulta): Resposta =
    val docsRelevantes = retrieval.recuperar(consulta, baseConhecimento)
    val resposta = generation.gerar(consulta, docsRelevantes)
    val relevancia = if docsRelevantes.isEmpty then 0.0 else docsRelevantes.length.toDouble / 3.0
    Resposta(consulta, docsRelevantes, resposta, relevancia)

@main def run(): Unit =
  val documentos = List(
    Documento("D001", "Política de Entrega", "A entrega de produtos é feita em até 7 dias úteis para todo o Brasil. O frete é calculado no checkout.", "Logística"),
    Documento("D002", "Formas de Pagamento", "Aceitamos pagamento via cartão de crédito, débito, PIX e boleto bancário. Parcele em até 12x.", "Financeiro"),
    Documento("D003", "Política de Devolução", "Você pode devolver o produto em até 30 dias após a compra. O valor será reembolsado em até 15 dias.", "Atendimento"),
    Documento("D004", "Garantia de Produtos", "Todos os produtos possuem garantia de 12 meses contra defeitos de fabricação.", "Produto"),
    Documento("D005", "Rastreamento de Pedidos", "Após o envio, você receberá um código de rastreamento por email para acompanhar a entrega.", "Logística")
  )

  println("=== RAG com Keyword Strategy + Template Generation ===")
  val rag1 = RAGSystem(KeywordStrategy(), TemplateGeneration())
  rag1.adicionarDocumentos(documentos)

  val consulta1 = Consulta("Como funciona a entrega?")
  val resposta1 = rag1.consultar(consulta1)
  println(s"Consulta: ${consulta1.texto}")
  println(s"Resposta:\n${resposta1.resposta}")
  println()

  println("=== RAG com Semantic Strategy + Template Generation ===")
  val rag2 = RAGSystem(SemanticStrategy(), TemplateGeneration())
  rag2.adicionarDocumentos(documentos)

  val consulta2 = Consulta("Quais são as formas de pagar?")
  val resposta2 = rag2.consultar(consulta2)
  println(s"Consulta: ${consulta2.texto}")
  println(s"Resposta:\n${resposta2.resposta}")
  println()

  println("=== RAG com Hybrid Strategy + Summary Generation ===")
  val rag3 = RAGSystem(HybridStrategy(), SummaryGeneration())
  rag3.adicionarDocumentos(documentos)

  val consulta3 = Consulta("Informações sobre garantia e devolução")
  val resposta3 = rag3.consultar(consulta3)
  println(s"Consulta: ${consulta3.texto}")
  println(s"Resposta:\n${resposta3.resposta}")
