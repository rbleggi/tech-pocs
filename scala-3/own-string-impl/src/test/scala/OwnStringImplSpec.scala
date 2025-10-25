package com.rbleggi.ownstringimpl

class OwnStringImplSpec {
  test("MyString should convert to array correctly") {
    val str = new MyString("hello")
    val arr = str.toArray
    assert(arr.length == 5)
    assert(arr(0) == 'h')
    assert(arr(4) == 'o')
  }

  test("MyString should reverse correctly") {
    val str = new MyString("hello")
    val reversed = str.reverse
    assert(reversed.value == "olleh")
  }

  test("MyString should calculate length correctly") {
    val str = new MyString("hello")
    assert(str.length == 5)

    val empty = new MyString("")
    assert(empty.length == 0)
  }

  test("MyString should get char at index") {
    val str = new MyString("hello")
    assert(str.charAt(0) == 'h')
    assert(str.charAt(4) == 'o')
  }

  test("MyString charAt should throw on invalid index") {
    val str = new MyString("hello")
    assertThrows[IndexOutOfBoundsException] {
      str.charAt(10)
    }
  }

  test("MyString should check equality with another MyString") {
    val str1 = new MyString("hello")
    val str2 = new MyString("hello")
    val str3 = new MyString("world")

    assert(str1.equals(str2))
    assert(!str1.equals(str3))
  }

  test("MyString should check equality with String") {
    val str = new MyString("hello")
    assert(str.equals("hello"))
    assert(!str.equals("world"))
  }

  test("MyString should check if empty") {
    val empty = new MyString("")
    val notEmpty = new MyString("hello")

    assert(empty.isEmpty)
    assert(!notEmpty.isEmpty)
  }

  test("MyString should replace characters") {
    val str = new MyString("hello")
    val replaced = str.replace('l', 'x')
    assert(replaced.value == "hexxo")
  }

  test("MyString should iterate over characters") {
    val str = new MyString("abc")
    val chars = scala.collection.mutable.ListBuffer[Char]()
    str.foreach(c => chars += c)

    assert(chars.toList == List('a', 'b', 'c'))
  }

  test("MyString iterator should work correctly") {
    val str = new MyString("abc")
    val it = str.iterator

    assert(it.hasNext)
    assert(it.next() == 'a')
    assert(it.next() == 'b')
    assert(it.next() == 'c')
    assert(!it.hasNext)
  }
}

