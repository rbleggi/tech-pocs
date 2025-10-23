package com.rbleggi.loggerrouter

import org.scalatest.funsuite.AnyFunSuite
import java.io.File
import scala.io.Source

class LoggerRouterSpec extends AnyFunSuite {
  test("ConsoleStrategy should log to console") {
    val strategy = new ConsoleStrategy()
    assertNoException {
      strategy.log(LogLevel.INFO, "Test message")
    }
  }

  test("FileSystemStrategy should log to file") {
    val testFile = "test-log.txt"
    val strategy = new FileSystemStrategy(testFile)

    strategy.log(LogLevel.INFO, "Test message")

    val file = new File(testFile)
    assert(file.exists())

    val content = Source.fromFile(testFile).mkString
    assert(content.contains("INFO"))
    assert(content.contains("Test message"))

    file.delete()
  }

  test("ELKStrategy should log to ELK endpoint") {
    val strategy = new ELKStrategy("http://localhost:9200")
    assertNoException {
      strategy.log(LogLevel.ERROR, "Error message")
    }
  }

  test("LoggerRouter should route to multiple strategies") {
    var consoleLogged = false
    var elkLogged = false

    val consoleStrategy = new LogStrategy {
      override def log(level: LogLevel, msg: String): Unit = consoleLogged = true
    }

    val elkStrategy = new LogStrategy {
      override def log(level: LogLevel, msg: String): Unit = elkLogged = true
    }

    val router = new LoggerRouter(List(consoleStrategy, elkStrategy))
    router.log(LogLevel.INFO, "Test message")

    assert(consoleLogged)
    assert(elkLogged)
  }

  test("LogLevel should have INFO and ERROR") {
    assert(LogLevel.INFO != LogLevel.ERROR)
  }

  def assertNoException(block: => Unit): Unit = {
    try {
      block
      assert(true)
    } catch {
      case _: Exception => fail("Unexpected exception")
    }
  }
}
