package com.rbleggi.multimodal

sealed trait TipoEntrada
case class EntradaTexto(texto: String) extends TipoEntrada
case class EntradaNumero(numero: Double) extends TipoEntrada
case class EntradaCategoria(categoria: String) extends TipoEntrada

case class Analise(tipo: String, resultado: String, confianca: Double)

trait ProcessadorStrategy:
  def processar(entrada: TipoEntrada): Analise

class ProcessadorTexto extends ProcessadorStrategy:
  def processar(entrada: TipoEntrada): Analise =
    entrada match
      case EntradaTexto(texto) =>
        val palavras = texto.split("\\s+").length
        val sentimento = if texto.toLowerCase.contains("bom") || texto.toLowerCase.contains("otimo") then "positivo"
                        else if texto.toLowerCase.contains("ruim") || texto.toLowerCase.contains("pessimo") then "negativo"
                        else "neutro"
        Analise("texto", s"$palavras palavras, sentimento $sentimento", 0.85)
      case _ =>
        Analise("texto", "entrada invalida", 0.0)

class ProcessadorNumero extends ProcessadorStrategy:
  def processar(entrada: TipoEntrada): Analise =
    entrada match
      case EntradaNumero(numero) =>
        val categoria = if numero < 0 then "negativo"
                       else if numero == 0 then "zero"
                       else if numero < 100 then "pequeno"
                       else if numero < 1000 then "medio"
                       else "grande"
        Analise("numero", s"valor $numero classificado como $categoria", 0.95)
      case _ =>
        Analise("numero", "entrada invalida", 0.0)

class ProcessadorCategoria extends ProcessadorStrategy:
  private val categorias = Map(
    "sp" -> "Sao Paulo",
    "rj" -> "Rio de Janeiro",
    "mg" -> "Minas Gerais",
    "ba" -> "Bahia"
  )

  def processar(entrada: TipoEntrada): Analise =
    entrada match
      case EntradaCategoria(cat) =>
        val nomeCidade = categorias.get(cat.toLowerCase)
        nomeCidade match
          case Some(nome) => Analise("categoria", s"estado identificado: $nome", 1.0)
          case None => Analise("categoria", "estado desconhecido", 0.3)
      case _ =>
        Analise("categoria", "entrada invalida", 0.0)

class SistemaMultiModal:
  private var processadores: Map[String, ProcessadorStrategy] = Map(
    "texto" -> ProcessadorTexto(),
    "numero" -> ProcessadorNumero(),
    "categoria" -> ProcessadorCategoria()
  )

  def processar(entrada: TipoEntrada, modo: String): Analise =
    processadores.get(modo) match
      case Some(processador) => processador.processar(entrada)
      case None => Analise("erro", "modo desconhecido", 0.0)

  def adicionarProcessador(modo: String, processador: ProcessadorStrategy): Unit =
    processadores = processadores + (modo -> processador)

@main def run(): Unit =
  val sistema = SistemaMultiModal()

  val texto = EntradaTexto("O produto e muito bom e atende bem")
  val numero = EntradaNumero(1500.0)
  val categoria = EntradaCategoria("sp")

  val analiseTexto = sistema.processar(texto, "texto")
  println(s"Analise Texto: ${analiseTexto.resultado} (confianca: ${analiseTexto.confianca})")

  val analiseNumero = sistema.processar(numero, "numero")
  println(s"Analise Numero: ${analiseNumero.resultado} (confianca: ${analiseNumero.confianca})")

  val analiseCategoria = sistema.processar(categoria, "categoria")
  println(s"Analise Categoria: ${analiseCategoria.resultado} (confianca: ${analiseCategoria.confianca})")
