package com.rbleggi.euler

class EulerSpec {
  test("countDivisors should count divisors correctly for small numbers") {
    assert(countDivisors(1) == 1)
    assert(countDivisors(3) == 2)
    assert(countDivisors(6) == 4)
    assert(countDivisors(10) == 4)
    assert(countDivisors(15) == 4)
    assert(countDivisors(21) == 4)
    assert(countDivisors(28) == 6)
  }

  test("countDivisors should handle prime numbers") {
    assert(countDivisors(2) == 2)
    assert(countDivisors(3) == 2)
    assert(countDivisors(5) == 2)
    assert(countDivisors(7) == 2)
  }

  test("firstTriangleWithDivisors should find triangle with more than 5 divisors") {
    val result = firstTriangleWithDivisors(5)
    assert(result == 28)
    assert(countDivisors(result) > 5)
  }

  test("firstTriangleWithDivisors should work for small limits") {
    val result = firstTriangleWithDivisors(1)
    assert(countDivisors(result) > 1)
  }
}
