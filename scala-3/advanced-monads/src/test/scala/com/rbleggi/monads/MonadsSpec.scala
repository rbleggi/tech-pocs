package com.rbleggi.monads

import org.scalatest.funsuite.AnyFunSuite

class MonadsSpec extends AnyFunSuite:

  test("IO monad should execute side effect"):
    val io = IO(() => 42)
    assert(io.run() == 42)

  test("IO monad should map correctly"):
    val io = IO(() => 10).map(_ * 2)
    assert(io.run() == 20)

  test("IO monad should flatMap correctly"):
    val io = IO(() => 5).flatMap(x => IO(() => x + 3))
    assert(io.run() == 8)

  test("State monad should maintain state"):
    val state = State[Int, String](s => (s.toString, s + 1))
    val (value, newState) = state.run(10)
    assert(value == "10")
    assert(newState == 11)

  test("State monad should compose with flatMap"):
    val state = for
      s1 <- State.get[Int]
      _ <- State.put(s1 + 5)
      s2 <- State.get[Int]
    yield s2

    val (value, finalState) = state.run(10)
    assert(value == 15)
    assert(finalState == 15)

  test("State monad should modify state"):
    val state = State.modify[Int](_ * 2).flatMap(_ => State.get[Int])
    val (value, finalState) = state.run(7)
    assert(value == 14)
    assert(finalState == 14)

  test("Reader monad should read environment"):
    val reader = Reader.ask[String]
    assert(reader.run("test") == "test")

  test("Reader monad should map correctly"):
    val reader = Reader.ask[String].map(_.toUpperCase)
    assert(reader.run("hello") == "HELLO")

  test("Reader monad should compose with flatMap"):
    val reader = for
      env <- Reader.ask[String]
      result <- Reader.pure[String, Int](env.length)
    yield result

    assert(reader.run("scala") == 5)

  test("Writer monad should accumulate logs"):
    val writer = Writer.tell(List("log1"))
      .flatMap(_ => Writer.tell(List("log2")))
    val (value, logs) = writer.run
    assert(logs == List("log1", "log2"))

  test("Writer monad should carry value and logs"):
    val writer = for
      _ <- Writer.tell(List("start"))
      result <- Writer.pure[List[String], Int](42)
      _ <- Writer.tell(List("end"))
    yield result

    val (value, logs) = writer.run
    assert(value == 42)
    assert(logs == List("start", "end"))

  test("depositar should increase balance"):
    val conta = ContaBancaria("João", 1000.0, "123.456.789-01")
    val (novoSaldo, contaAtualizada) = BancoMonads.depositar(500.0).run(conta)
    assert(novoSaldo == 1500.0)
    assert(contaAtualizada.saldo == 1500.0)

  test("sacar should decrease balance when sufficient funds"):
    val conta = ContaBancaria("Maria", 1000.0, "987.654.321-00")
    val (resultado, contaAtualizada) = BancoMonads.sacar(300.0).run(conta)
    assert(resultado == Some(300.0))
    assert(contaAtualizada.saldo == 700.0)

  test("sacar should fail when insufficient funds"):
    val conta = ContaBancaria("Carlos", 100.0, "111.222.333-44")
    val (resultado, contaAtualizada) = BancoMonads.sacar(200.0).run(conta)
    assert(resultado.isEmpty)
    assert(contaAtualizada.saldo == 100.0)

  test("transferir should succeed with sufficient funds"):
    val origem = ContaBancaria("Ana", 2000.0, "555.666.777-88")
    val destino = ContaBancaria("Pedro", 500.0, "999.888.777-66")
    val io = BancoMonads.transferir(origem, destino, 300.0)
    val mensagem = io.run()
    assert(mensagem.contains("Transferência"))
    assert(mensagem.contains("300.00"))

  test("transferir should fail with insufficient funds"):
    val origem = ContaBancaria("Joana", 100.0, "123.123.123-12")
    val destino = ContaBancaria("Roberto", 500.0, "456.456.456-45")
    val io = BancoMonads.transferir(origem, destino, 200.0)
    val mensagem = io.run()
    assert(mensagem.contains("insuficiente"))
