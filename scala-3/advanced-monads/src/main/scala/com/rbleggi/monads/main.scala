package com.rbleggi.monads

case class IO[A](run: () => A):
  def map[B](f: A => B): IO[B] =
    IO(() => f(run()))

  def flatMap[B](f: A => IO[B]): IO[B] =
    IO(() => f(run()).run())

object IO:
  def pure[A](a: A): IO[A] =
    IO(() => a)

case class State[S, A](run: S => (A, S)):
  def map[B](f: A => B): State[S, B] =
    State(s =>
      val (a, s2) = run(s)
      (f(a), s2)
    )

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State(s =>
      val (a, s2) = run(s)
      f(a).run(s2)
    )

object State:
  def pure[S, A](a: A): State[S, A] =
    State(s => (a, s))

  def get[S]: State[S, S] =
    State(s => (s, s))

  def put[S](s: S): State[S, Unit] =
    State(_ => ((), s))

  def modify[S](f: S => S): State[S, Unit] =
    State(s => ((), f(s)))

case class Reader[R, A](run: R => A):
  def map[B](f: A => B): Reader[R, B] =
    Reader(r => f(run(r)))

  def flatMap[B](f: A => Reader[R, B]): Reader[R, B] =
    Reader(r => f(run(r)).run(r))

object Reader:
  def pure[R, A](a: A): Reader[R, A] =
    Reader(_ => a)

  def ask[R]: Reader[R, R] =
    Reader(r => r)

case class Writer[W, A](run: (A, W)):
  def map[B](f: A => B): Writer[W, B] =
    val (a, w) = run
    Writer((f(a), w))

  def flatMap[B](f: A => Writer[W, B])(using m: Monoid[W]): Writer[W, B] =
    val (a, w1) = run
    val (b, w2) = f(a).run
    Writer((b, m.combine(w1, w2)))

object Writer:
  def pure[W, A](a: A)(using m: Monoid[W]): Writer[W, A] =
    Writer((a, m.empty))

  def tell[W](w: W): Writer[W, Unit] =
    Writer(((), w))

trait Monoid[A]:
  def empty: A
  def combine(x: A, y: A): A

object Monoid:
  given Monoid[List[String]] with
    def empty: List[String] = List.empty
    def combine(x: List[String], y: List[String]): List[String] = x ++ y

case class ContaBancaria(titular: String, saldo: Double, cpf: String)

object BancoMonads:
  def depositar(valor: Double): State[ContaBancaria, Double] =
    State.modify[ContaBancaria](conta =>
      conta.copy(saldo = conta.saldo + valor)
    ).flatMap(_ =>
      State.get[ContaBancaria].map(_.saldo)
    )

  def sacar(valor: Double): State[ContaBancaria, Option[Double]] =
    State.get[ContaBancaria].flatMap { conta =>
      if conta.saldo >= valor then
        State.modify[ContaBancaria](c => c.copy(saldo = c.saldo - valor))
          .map(_ => Some(valor))
      else
        State.pure(None)
    }

  def transferir(origem: ContaBancaria, destino: ContaBancaria, valor: Double): IO[String] =
    IO { () =>
      if origem.saldo >= valor then
        val novaOrigemSaldo = origem.saldo - valor
        val novoDestinoSaldo = destino.saldo + valor
        f"Transferência de R$$ $valor%.2f de ${origem.titular} para ${destino.titular} realizada"
      else
        "Saldo insuficiente para transferência"
    }

@main def run(): Unit =
  println("=== State Monad - Operações Bancárias ===")
  val conta = ContaBancaria("João Silva", 1000.0, "123.456.789-01")

  val operacoes = for
    saldo1 <- BancoMonads.depositar(500.0)
    saque <- BancoMonads.sacar(200.0)
    saldo2 <- State.get[ContaBancaria].map(_.saldo)
  yield (saldo1, saque, saldo2)

  val (resultado, contaFinal) = operacoes.run(conta)
  println(s"Titular: ${contaFinal.titular}")
  println(f"Saldo após depósito: R$$ ${resultado._1}%.2f")
  println(s"Saque: ${resultado._2}")
  println(f"Saldo final: R$$ ${resultado._3}%.2f")
  println()

  println("=== IO Monad - Transferência ===")
  val origem = ContaBancaria("Maria Santos", 2000.0, "987.654.321-00")
  val destino = ContaBancaria("Carlos Oliveira", 500.0, "111.222.333-44")
  val transferencia = BancoMonads.transferir(origem, destino, 300.0)
  println(transferencia.run())
  println()

  println("=== Writer Monad - Log de Operações ===")
  val operacaoComLog = for
    _ <- Writer.tell(List("Iniciando operação"))
    _ <- Writer.tell(List("Validando dados"))
    resultado <- Writer.pure[List[String], String]("Operação concluída")
    _ <- Writer.tell(List("Finalizando"))
  yield resultado

  val (valorFinal, logs) = operacaoComLog.run
  println(s"Resultado: $valorFinal")
  println("Logs:")
  logs.foreach(log => println(s"  - $log"))
