package com.rbleggi.parsercombinators

import org.scalatest.funsuite.AnyFunSuite

class ParserCombinatorsSpec extends AnyFunSuite:

  test("char parser should parse single character"):
    val parser = Parser.char('a')
    val result = parser.parse("abc")
    assert(result.isDefined)
    assert(result.get.value == 'a')
    assert(result.get.remaining == "bc")

  test("char parser should fail on wrong character"):
    val parser = Parser.char('a')
    val result = parser.parse("xyz")
    assert(result.isEmpty)

  test("string parser should parse string"):
    val parser = Parser.string("hello")
    val result = parser.parse("hello world")
    assert(result.isDefined)
    assert(result.get.value == "hello")
    assert(result.get.remaining == " world")

  test("string parser should fail on mismatch"):
    val parser = Parser.string("hello")
    val result = parser.parse("world")
    assert(result.isEmpty)

  test("digit parser should parse single digit"):
    val parser = Parser.digit
    val result = parser.parse("5abc")
    assert(result.isDefined)
    assert(result.get.value == 5)
    assert(result.get.remaining == "abc")

  test("digits parser should parse multiple digits"):
    val parser = Parser.digits
    val result = parser.parse("123abc")
    assert(result.isDefined)
    assert(result.get.value == 123)
    assert(result.get.remaining == "abc")

  test("map combinator should transform result"):
    val parser = Parser.digits.map(_ * 2)
    val result = parser.parse("10")
    assert(result.isDefined)
    assert(result.get.value == 20)

  test("sequential combinator should parse two parsers in sequence"):
    val parser = Parser.char('a') ~ Parser.char('b')
    val result = parser.parse("abc")
    assert(result.isDefined)
    assert(result.get.value == ('a', 'b'))
    assert(result.get.remaining == "c")

  test("alternative combinator should try first then second"):
    val parser = Parser.char('a') | Parser.char('b')
    val result1 = parser.parse("abc")
    assert(result1.isDefined)
    assert(result1.get.value == 'a')

    val result2 = parser.parse("bcd")
    assert(result2.isDefined)
    assert(result2.get.value == 'b')

  test("endereco parser should parse complete address"):
    val input = "Rua das Flores, 123, Sao Paulo, SP, 01234-567"
    val result = EnderecoParser.endereco.parse(input)
    assert(result.isDefined)
    val endereco = result.get.value
    assert(endereco.logradouro == "Rua das Flores")
    assert(endereco.numero == 123)
    assert(endereco.cidade == "Sao Paulo")
    assert(endereco.estado == "SP")
    assert(endereco.cep == "01234-567")

  test("endereco parser should parse Curitiba address"):
    val input = "Avenida Brasil, 456, Curitiba, PR, 80000-123"
    val result = EnderecoParser.endereco.parse(input)
    assert(result.isDefined)
    val endereco = result.get.value
    assert(endereco.logradouro == "Avenida Brasil")
    assert(endereco.numero == 456)
    assert(endereco.cidade == "Curitiba")
    assert(endereco.estado == "PR")

  test("endereco parser should fail on malformed input"):
    val input = "Invalid Address"
    val result = EnderecoParser.endereco.parse(input)
    assert(result.isEmpty)
