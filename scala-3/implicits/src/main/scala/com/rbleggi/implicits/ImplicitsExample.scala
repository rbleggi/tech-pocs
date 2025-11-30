package com.rbleggi.implicits

trait Ordering[A]:
  def compare(x: A, y: A): Int

object Ordering:
  given Ordering[Int] with
    def compare(x: Int, y: Int): Int = x - y

  given Ordering[String] with
    def compare(x: String, y: String): Int = x.compareTo(y)

  given Ordering[Double] with
    def compare(x: Double, y: Double): Int =
      if x < y then -1 else if x > y then 1 else 0

def sort[A](list: List[A])(using ord: Ordering[A]): List[A] =
  list.sortWith((a, b) => ord.compare(a, b) < 0)

case class Config(timeout: Int, retries: Int, debug: Boolean)

given defaultConfig: Config = Config(5000, 3, false)

def makeRequest(url: String)(using config: Config): String =
  s"Request to $url [timeout=${config.timeout}ms, retries=${config.retries}, debug=${config.debug}]"

trait Converter[From, To]:
  def convert(from: From): To

object Converter:
  given Converter[String, Int] with
    def convert(from: String): Int = from.toIntOption.getOrElse(0)

  given Converter[Int, String] with
    def convert(from: Int): String = from.toString

  given Converter[String, Boolean] with
    def convert(from: String): Boolean =
      from.toLowerCase match
        case "true" | "yes" | "1" => true
        case _ => false

  given Converter[Double, Int] with
    def convert(from: Double): Int = from.toInt

extension [A](value: A)
  def convertTo[B](using converter: Converter[A, B]): B =
    converter.convert(value)

extension (s: String)
  def toSnakeCase: String =
    s.replaceAll("([A-Z])", "_$1").toLowerCase.stripPrefix("_")

  def toCamelCase: String =
    s.split("_").map(_.capitalize).mkString

  def isNumeric: Boolean = s.forall(_.isDigit)

  def repeat(n: Int): String = s * n

extension (n: Int)
  def times(action: => Unit): Unit =
    (1 to n).foreach(_ => action)

  def seconds: Duration = Duration(n, "seconds")
  def minutes: Duration = Duration(n * 60, "seconds")

case class Duration(value: Int, unit: String):
  override def toString: String = s"$value $unit"

extension [A](list: List[A])
  def secondOption: Option[A] =
    if list.length >= 2 then Some(list(1)) else None

  def splitAt(predicate: A => Boolean): (List[A], List[A]) =
    list.partition(predicate)

trait Printable[A]:
  def print(value: A): String

object Printable:
  given Printable[Int] with
    def print(value: Int): String = s"Integer: $value"

  given Printable[String] with
    def print(value: String): String = s"String: '$value'"

  given [A](using p: Printable[A]): Printable[List[A]] with
    def print(value: List[A]): String =
      s"List[${value.map(p.print).mkString(", ")}]"

def printValue[A](value: A)(using p: Printable[A]): Unit =
  println(p.print(value))

trait ExecutionContext:
  def name: String

object ExecutionContext:
  given global: ExecutionContext with
    def name: String = "global-execution-context"

def runAsync(task: => Unit)(using ec: ExecutionContext): Unit =
  println(s"Running task on ${ec.name}")
  task

@main def implicitsExample(): Unit =
  println("=== Implicits/Givens Example (Scala 3) ===\n")

  println("1. Given/Using - Context Parameters")
  println(sort(List(3, 1, 4, 1, 5, 9, 2, 6)))
  println(sort(List("banana", "apple", "cherry", "date")))
  println(sort(List(3.14, 2.71, 1.41, 1.73)))

  println("\n2. Implicit Config Pattern")
  println(makeRequest("https://api.example.com/users"))

  given customConfig: Config = Config(10000, 5, true)
  println(makeRequest("https://api.example.com/data"))

  println("\n3. Type Conversions")
  println(s"'42' to Int: ${"42".convertTo[Int]}")
  println(s"42 to String: ${42.convertTo[String]}")
  println(s"'true' to Boolean: ${"true".convertTo[Boolean]}")
  println(s"3.14 to Int: ${3.14.convertTo[Int]}")

  println("\n4. Extension Methods - String")
  println(s"'HelloWorld' to snake_case: ${"HelloWorld".toSnakeCase}")
  println(s"'hello_world' to CamelCase: ${"hello_world".toCamelCase}")
  println(s"'12345' is numeric: ${"12345".isNumeric}")
  println(s"'abc' repeated 3 times: ${"abc".repeat(3)}")

  println("\n5. Extension Methods - Int")
  print("Executing 3 times: ")
  3.times { print("* ") }
  println()
  println(s"5 seconds: ${5.seconds}")
  println(s"2 minutes: ${2.minutes}")

  println("\n6. Extension Methods - List")
  val numbers = List(1, 2, 3, 4, 5)
  println(s"Second element: ${numbers.secondOption}")
  val (evens, odds) = numbers.splitAt(_ % 2 == 0)
  println(s"Evens: $evens, Odds: $odds")

  println("\n7. Printable Type Class")
  printValue(42)
  printValue("Hello")
  printValue(List(1, 2, 3))

  println("\n8. Execution Context")
  runAsync {
    println("  Task executed!")
  }

  println("\n=== Implicits Benefits ===")
  println("1. Dependency injection without frameworks")
  println("2. Extension methods for any type")
  println("3. Type class pattern implementation")
  println("4. Automatic conversions")
  println("5. Reduce boilerplate code")

