package com.rbleggi.assistant

case class Consulta(tipo: String, pergunta: String)

case class Resposta(tipo: String, resposta: String, confianca: Double)

trait ManipuladorConsulta:
  def processar(consulta: Consulta): Resposta

class ManipuladorMatematica extends ManipuladorConsulta:
  def processar(consulta: Consulta): Resposta =
    val pergunta = consulta.pergunta.toLowerCase
    try
      if pergunta.contains("soma") || pergunta.contains("+") then
        val numeros = pergunta.split("\\D+").filter(_.nonEmpty).map(_.toInt)
        if numeros.length >= 2 then
          val soma = numeros.sum
          Resposta("matematica", s"A soma e $soma", 1.0)
        else
          Resposta("matematica", "Nao encontrei numeros suficientes", 0.3)
      else if pergunta.contains("multiplica") || pergunta.contains("*") then
        val numeros = pergunta.split("\\D+").filter(_.nonEmpty).map(_.toInt)
        if numeros.length >= 2 then
          val produto = numeros.product
          Resposta("matematica", s"O produto e $produto", 1.0)
        else
          Resposta("matematica", "Nao encontrei numeros suficientes", 0.3)
      else
        Resposta("matematica", "Operacao nao reconhecida", 0.2)
    catch
      case _: Exception => Resposta("matematica", "Erro ao processar numeros", 0.1)

class ManipuladorTexto extends ManipuladorConsulta:
  def processar(consulta: Consulta): Resposta =
    val pergunta = consulta.pergunta.toLowerCase
    if pergunta.contains("quantas palavras") then
      val palavras = consulta.pergunta.split("\\s+").length
      Resposta("texto", s"A pergunta tem $palavras palavras", 0.9)
    else if pergunta.contains("maiuscula") then
      val maiuscula = consulta.pergunta.toUpperCase
      Resposta("texto", maiuscula, 1.0)
    else if pergunta.contains("minuscula") then
      val minuscula = consulta.pergunta.toLowerCase
      Resposta("texto", minuscula, 1.0)
    else
      Resposta("texto", "Operacao de texto nao reconhecida", 0.3)

class ManipuladorDados extends ManipuladorConsulta:
  private val dados = Map(
    "sao paulo" -> "Populacao: 12 milhoes, estado: SP",
    "curitiba" -> "Populacao: 1.9 milhoes, estado: PR",
    "belo horizonte" -> "Populacao: 2.5 milhoes, estado: MG"
  )

  def processar(consulta: Consulta): Resposta =
    val pergunta = consulta.pergunta.toLowerCase
    val cidadeEncontrada = dados.keys.find(c => pergunta.contains(c))

    cidadeEncontrada match
      case Some(cidade) =>
        Resposta("dados", dados(cidade), 0.95)
      case None =>
        Resposta("dados", "Cidade nao encontrada na base de dados", 0.2)

class AssistentePessoal:
  private val manipuladores = Map(
    "matematica" -> ManipuladorMatematica(),
    "texto" -> ManipuladorTexto(),
    "dados" -> ManipuladorDados()
  )

  def consultar(consulta: Consulta): Resposta =
    manipuladores.get(consulta.tipo) match
      case Some(manipulador) => manipulador.processar(consulta)
      case None => Resposta("erro", "Tipo de consulta nao suportado", 0.0)

  def consultarAutomatico(pergunta: String): Resposta =
    val perguntaLower = pergunta.toLowerCase
    val tipo = if perguntaLower.matches(".*\\d+.*") && (perguntaLower.contains("soma") || perguntaLower.contains("multiplica")) then
                 "matematica"
               else if perguntaLower.contains("maiuscula") || perguntaLower.contains("minuscula") || perguntaLower.contains("palavras") then
                 "texto"
               else if perguntaLower.contains("sao paulo") || perguntaLower.contains("curitiba") || perguntaLower.contains("belo horizonte") then
                 "dados"
               else
                 "desconhecido"

    consultar(Consulta(tipo, pergunta))

@main def run(): Unit =
  val assistente = AssistentePessoal()

  val consulta1 = Consulta("matematica", "Qual a soma de 10 e 20?")
  val resposta1 = assistente.consultar(consulta1)
  println(s"Resposta 1: ${resposta1.resposta} (confianca: ${resposta1.confianca})")

  val consulta2 = Consulta("texto", "Converta para maiuscula: joao maria")
  val resposta2 = assistente.consultar(consulta2)
  println(s"Resposta 2: ${resposta2.resposta}")

  val consulta3 = Consulta("dados", "Informacoes sobre sao paulo")
  val resposta3 = assistente.consultar(consulta3)
  println(s"Resposta 3: ${resposta3.resposta}")
