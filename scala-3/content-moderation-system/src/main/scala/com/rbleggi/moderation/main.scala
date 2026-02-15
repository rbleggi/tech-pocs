package com.rbleggi.moderation

case class Conteudo(id: String, texto: String, autor: String)

case class ResultadoModeracao(aprovado: Boolean, motivo: String, severidade: String)

trait ModeradorStrategy:
  def moderar(conteudo: Conteudo): ResultadoModeracao

class FiltroKeywords extends ModeradorStrategy:
  private val palavrasProibidas = Set(
    "spam", "golpe", "fraude", "scam"
  )

  def moderar(conteudo: Conteudo): ResultadoModeracao =
    val textoLower = conteudo.texto.toLowerCase
    val encontradas = palavrasProibidas.filter(p => textoLower.contains(p))

    if encontradas.nonEmpty then
      ResultadoModeracao(false, s"palavras proibidas: ${encontradas.mkString(", ")}", "alta")
    else
      ResultadoModeracao(true, "nenhuma palavra proibida encontrada", "baixa")

class FiltroRegex extends ModeradorStrategy:
  private val padroes = List(
    "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}".r,
    "\\d{11}".r,
    "R\\$\\s*\\d+".r
  )

  def moderar(conteudo: Conteudo): ResultadoModeracao =
    val encontrados = padroes.flatMap(p => p.findAllIn(conteudo.texto))

    if encontrados.nonEmpty then
      ResultadoModeracao(false, "dados sensiveis detectados", "media")
    else
      ResultadoModeracao(true, "nenhum dado sensivel encontrado", "baixa")

class FiltroTamanho extends ModeradorStrategy:
  private val tamanhoMinimo = 10
  private val tamanhoMaximo = 500

  def moderar(conteudo: Conteudo): ResultadoModeracao =
    val tamanho = conteudo.texto.length

    if tamanho < tamanhoMinimo then
      ResultadoModeracao(false, s"texto muito curto ($tamanho caracteres)", "baixa")
    else if tamanho > tamanhoMaximo then
      ResultadoModeracao(false, s"texto muito longo ($tamanho caracteres)", "media")
    else
      ResultadoModeracao(true, "tamanho adequado", "baixa")

class SistemaModeracao:
  private var moderadores: List[ModeradorStrategy] = List(
    FiltroKeywords(),
    FiltroRegex(),
    FiltroTamanho()
  )

  def moderarConteudo(conteudo: Conteudo): List[ResultadoModeracao] =
    moderadores.map(m => m.moderar(conteudo))

  def aprovar(conteudo: Conteudo): Boolean =
    val resultados = moderarConteudo(conteudo)
    resultados.forall(_.aprovado)

  def adicionarModerador(moderador: ModeradorStrategy): Unit =
    moderadores = moderadores :+ moderador

@main def run(): Unit =
  val sistema = SistemaModeracao()

  val conteudo1 = Conteudo("1", "Mensagem de teste normal para o sistema", "Joao")
  val conteudo2 = Conteudo("2", "Isso e um spam de golpe", "Maria")
  val conteudo3 = Conteudo("3", "Curto", "Carlos")

  println(s"Conteudo 1 aprovado: ${sistema.aprovar(conteudo1)}")
  println(s"Conteudo 2 aprovado: ${sistema.aprovar(conteudo2)}")
  println(s"Conteudo 3 aprovado: ${sistema.aprovar(conteudo3)}")

  val resultados = sistema.moderarConteudo(conteudo2)
  resultados.foreach { r =>
    println(s"  - ${r.motivo} (severidade: ${r.severidade})")
  }
