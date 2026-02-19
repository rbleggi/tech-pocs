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

case class BankAccount(holder: String, balance: Double, cpf: String)

object BankMonads:
  def deposit(amount: Double): State[BankAccount, Double] =
    State.modify[BankAccount](account =>
      account.copy(balance = account.balance + amount)
    ).flatMap(_ =>
      State.get[BankAccount].map(_.balance)
    )

  def withdraw(amount: Double): State[BankAccount, Option[Double]] =
    State.get[BankAccount].flatMap { account =>
      if account.balance >= amount then
        State.modify[BankAccount](c => c.copy(balance = c.balance - amount))
          .map(_ => Some(amount))
      else
        State.pure(None)
    }

  def transfer(origin: BankAccount, destination: BankAccount, amount: Double): IO[String] =
    IO { () =>
      if origin.balance >= amount then
        val newOriginBalance = origin.balance - amount
        val newDestBalance = destination.balance + amount
        f"Transfer of R$$ $amount%.2f from ${origin.holder} to ${destination.holder} completed"
      else
        "Insufficient balance for transfer"
    }

@main def run(): Unit =
  println("=== State Monad - Banking Operations ===")
  val account = BankAccount("Joao Silva", 1000.0, "123.456.789-01")

  val operations = for
    balance1 <- BankMonads.deposit(500.0)
    withdrawal <- BankMonads.withdraw(200.0)
    balance2 <- State.get[BankAccount].map(_.balance)
  yield (balance1, withdrawal, balance2)

  val (result, finalAccount) = operations.run(account)
  println(s"Holder: ${finalAccount.holder}")
  println(f"Balance after deposit: R$$ ${result._1}%.2f")
  println(s"Withdrawal: ${result._2}")
  println(f"Final balance: R$$ ${result._3}%.2f")
  println()

  println("=== IO Monad - Transfer ===")
  val origin = BankAccount("Maria Santos", 2000.0, "987.654.321-00")
  val destination = BankAccount("Carlos Oliveira", 500.0, "111.222.333-44")
  val transferOperation = BankMonads.transfer(origin, destination, 300.0)
  println(transferOperation.run())
  println()

  println("=== Writer Monad - Operation Logs ===")
  val operationWithLog = for
    _ <- Writer.tell(List("Starting operation"))
    _ <- Writer.tell(List("Validating data"))
    result <- Writer.pure[List[String], String]("Operation completed")
    _ <- Writer.tell(List("Finalizing"))
  yield result

  val (finalValue, logs) = operationWithLog.run
  println(s"Result: $finalValue")
  println("Logs:")
  logs.foreach(log => println(s"  - $log"))
