package com.rbleggi.parsercombinators

case class ParseResult[+T](value: T, remaining: String)

trait Parser[+T]:
  def parse(input: String): Option[ParseResult[T]]

  def map[U](f: T => U): Parser[U] =
    val self = this
    new Parser[U]:
      def parse(input: String): Option[ParseResult[U]] =
        self.parse(input).map(r => ParseResult(f(r.value), r.remaining))

  def flatMap[U](f: T => Parser[U]): Parser[U] =
    val self = this
    new Parser[U]:
      def parse(input: String): Option[ParseResult[U]] =
        self.parse(input).flatMap(r => f(r.value).parse(r.remaining))

  def ~[U](that: Parser[U]): Parser[(T, U)] =
    this.flatMap(a => that.map(b => (a, b)))

  def |[U >: T](that: Parser[U]): Parser[U] =
    val self = this
    new Parser[U]:
      def parse(input: String): Option[ParseResult[U]] =
        self.parse(input).orElse(that.parse(input))

object Parser:
  def char(c: Char): Parser[Char] = new Parser[Char]:
    def parse(input: String): Option[ParseResult[Char]] =
      if input.nonEmpty && input.head == c then
        Some(ParseResult(c, input.tail))
      else
        None

  def string(s: String): Parser[String] = new Parser[String]:
    def parse(input: String): Option[ParseResult[String]] =
      if input.startsWith(s) then
        Some(ParseResult(s, input.drop(s.length)))
      else
        None

  def regex(pattern: String): Parser[String] = new Parser[String]:
    def parse(input: String): Option[ParseResult[String]] =
      val r = pattern.r
      r.findPrefixOf(input).map(matched => ParseResult(matched, input.drop(matched.length)))

  def digit: Parser[Int] =
    regex("[0-9]").map(_.toInt)

  def digits: Parser[Int] =
    regex("[0-9]+").map(_.toInt)

case class Address(
  street: String,
  number: Int,
  city: String,
  state: String,
  zipCode: String
)

object AddressParser:
  import Parser.*

  val street: Parser[String] =
    regex("[A-Za-z ]+")

  val number: Parser[Int] =
    digits

  val city: Parser[String] =
    regex("[A-Za-z ]+")

  val state: Parser[String] =
    regex("[A-Z]{2}")

  val zipCode: Parser[String] =
    regex("[0-9]{5}-[0-9]{3}")

  val address: Parser[Address] =
    for
      str <- street
      _ <- string(", ")
      num <- number
      _ <- string(", ")
      cit <- city
      _ <- string(", ")
      st <- state
      _ <- string(", ")
      zip <- zipCode
    yield Address(str, num, cit, st, zip)

@main def run(): Unit =
  val addresses = List(
    "Rua das Flores, 123, Sao Paulo, SP, 01234-567",
    "Avenida Brasil, 456, Curitiba, PR, 80000-123",
    "Rua dos Pinheiros, 789, Belo Horizonte, MG, 30100-000"
  )

  addresses.foreach { input =>
    AddressParser.address.parse(input) match
      case Some(ParseResult(address, remaining)) =>
        println(s"Parsed: $address")
        if remaining.nonEmpty then
          println(s"Remaining: '$remaining'")
      case None =>
        println(s"Failed to parse: $input")
    println()
  }
