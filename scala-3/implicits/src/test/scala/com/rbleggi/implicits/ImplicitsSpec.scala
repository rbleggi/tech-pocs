package com.rbleggi.implicits

import org.scalatest.funsuite.AnyFunSuite

class ImplicitsSpec extends AnyFunSuite:

  test("sort should sort Int list"):
    val result = sort(List(3, 1, 4, 1, 5, 9, 2, 6))
    assert(result == List(1, 1, 2, 3, 4, 5, 6, 9))

  test("sort should sort String list"):
    val result = sort(List("banana", "apple", "cherry"))
    assert(result == List("apple", "banana", "cherry"))

  test("sort should sort Double list"):
    val result = sort(List(3.14, 1.41, 2.71))
    assert(result == List(1.41, 2.71, 3.14))

  test("makeRequest should use default config"):
    val result = makeRequest("https://example.com")
    assert(result.contains("timeout=5000"))
    assert(result.contains("retries=3"))
    assert(result.contains("debug=false"))

  test("makeRequest should use custom config"):
    given customConfig: Config = Config(10000, 5, true)
    val result = makeRequest("https://example.com")
    assert(result.contains("timeout=10000"))
    assert(result.contains("retries=5"))
    assert(result.contains("debug=true"))

  test("String to Int conversion"):
    assert("42".convertTo[Int] == 42)
    assert("abc".convertTo[Int] == 0)

  test("Int to String conversion"):
    assert(42.convertTo[String] == "42")

  test("String to Boolean conversion"):
    assert("true".convertTo[Boolean] == true)
    assert("yes".convertTo[Boolean] == true)
    assert("1".convertTo[Boolean] == true)
    assert("false".convertTo[Boolean] == false)
    assert("no".convertTo[Boolean] == false)

  test("Double to Int conversion"):
    assert(3.14.convertTo[Int] == 3)
    assert(9.99.convertTo[Int] == 9)

  test("String toSnakeCase extension"):
    assert("HelloWorld".toSnakeCase == "hello_world")
    assert("MyClassName".toSnakeCase == "my_class_name")

  test("String toCamelCase extension"):
    assert("hello_world".toCamelCase == "HelloWorld")
    assert("my_class_name".toCamelCase == "MyClassName")

  test("String isNumeric extension"):
    assert("12345".isNumeric == true)
    assert("123abc".isNumeric == false)
    assert("".isNumeric == true)

  test("String repeat extension"):
    assert("ab".repeat(3) == "ababab")
    assert("x".repeat(5) == "xxxxx")

  test("Int times extension"):
    var count = 0
    3.times { count += 1 }
    assert(count == 3)

  test("Int seconds extension"):
    val duration = 5.seconds
    assert(duration.value == 5)
    assert(duration.unit == "seconds")

  test("Int minutes extension"):
    val duration = 2.minutes
    assert(duration.value == 120)
    assert(duration.unit == "seconds")

  test("List secondOption extension"):
    assert(List(1, 2, 3).secondOption == Some(2))
    assert(List(1).secondOption == None)
    assert(List.empty[Int].secondOption == None)

  test("List splitAt extension"):
    val (evens, odds) = List(1, 2, 3, 4, 5).splitAt(_ % 2 == 0)
    assert(evens == List(2, 4))
    assert(odds == List(1, 3, 5))

  test("Printable for Int"):
    val printable = summon[Printable[Int]]
    assert(printable.print(42) == "Integer: 42")

  test("Printable for String"):
    val printable = summon[Printable[String]]
    assert(printable.print("test") == "String: 'test'")

  test("Printable for List[Int]"):
    val printable = summon[Printable[List[Int]]]
    val result = printable.print(List(1, 2, 3))
    assert(result.contains("Integer: 1"))
    assert(result.contains("Integer: 2"))
    assert(result.contains("Integer: 3"))

  test("ExecutionContext global should have name"):
    val ec = summon[ExecutionContext]
    assert(ec.name == "global-execution-context")

  test("Config should store values"):
    val config = Config(5000, 3, false)
    assert(config.timeout == 5000)
    assert(config.retries == 3)
    assert(config.debug == false)

  test("Duration should format correctly"):
    val duration = Duration(10, "seconds")
    assert(duration.toString == "10 seconds")
