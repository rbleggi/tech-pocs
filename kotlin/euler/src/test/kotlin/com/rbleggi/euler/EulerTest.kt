package com.rbleggi.euler

import kotlin.test.*
import java.math.BigInteger

class P01Test {
    @Test
    fun `sum of multiples of 3 or 5 below 10 equals 23`() {
        val result = (1 until 10).filter { n -> n % 3 == 0 || n % 5 == 0 }.sum()
        assertEquals(23, result)
    }

    @Test
    fun `sum of multiples of 3 or 5 below 1000 equals 233168`() {
        val result = (1 until 1000).filter { n -> n % 3 == 0 || n % 5 == 0 }.sum()
        assertEquals(233168, result)
    }
}

class P02Test {
    private fun generateFibs() = sequence {
        var a = 1
        var b = 2
        yield(a)
        yield(b)
        while (true) {
            val next = a + b
            yield(next)
            a = b
            b = next
        }
    }

    @Test
    fun `first few fibonacci numbers are correct`() {
        val fibs = generateFibs().take(5).toList()
        assertEquals(listOf(1, 2, 3, 5, 8), fibs)
    }

    @Test
    fun `sum of even fibonacci numbers not exceeding 4 million equals 4613732`() {
        val result = generateFibs().takeWhile { it <= 4000000 }.filter { it % 2 == 0 }.sum()
        assertEquals(4613732, result)
    }
}

class P03Test {
    private fun largestPrimeFactor(n: Long): Long {
        var num = n
        var factor = 2L
        var lastFactor = 1L
        while (factor * factor <= num) {
            if (num % factor == 0L) {
                lastFactor = factor
                num /= factor
                while (num % factor == 0L) {
                    num /= factor
                }
            }
            factor += 1
        }
        return if (num > 1) num else lastFactor
    }

    @Test
    fun `largest prime factor of 13195 equals 29`() {
        assertEquals(29L, largestPrimeFactor(13195L))
    }

    @Test
    fun `largest prime factor of 600851475143 equals 6857`() {
        assertEquals(6857L, largestPrimeFactor(600851475143L))
    }
}

class P04Test {
    private fun isPalindrome(n: Int): Boolean {
        val s = n.toString()
        return s == s.reversed()
    }

    private fun largestPalindromeProduct(): Int {
        var maxPalindrome = 0
        for (i in 999 downTo 100) {
            for (j in i downTo 100) {
                val product = i * j
                if (isPalindrome(product) && product > maxPalindrome) {
                    maxPalindrome = product
                }
            }
        }
        return maxPalindrome
    }

    @Test
    fun `9009 is a palindrome`() {
        assertTrue(isPalindrome(9009))
    }

    @Test
    fun `1234 is not a palindrome`() {
        assertFalse(isPalindrome(1234))
    }

    @Test
    fun `largest palindrome from product of two 3-digit numbers equals 906609`() {
        assertEquals(906609, largestPalindromeProduct())
    }
}

class P05Test {
    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    private fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    @Test
    fun `gcd of 12 and 8 equals 4`() {
        assertEquals(4L, gcd(12L, 8L))
    }

    @Test
    fun `lcm of 4 and 6 equals 12`() {
        assertEquals(12L, lcm(4L, 6L))
    }

    @Test
    fun `smallest number evenly divisible by 1 to 10 equals 2520`() {
        val result = (1L..10L).reduce { acc, i -> lcm(acc, i) }
        assertEquals(2520L, result)
    }

    @Test
    fun `smallest number evenly divisible by 1 to 20 equals 232792560`() {
        val result = (1L..20L).reduce { acc, i -> lcm(acc, i) }
        assertEquals(232792560L, result)
    }
}

class P06Test {
    @Test
    fun `sum of squares of first 10 natural numbers equals 385`() {
        val result = (1..10).sumOf { it * it }
        assertEquals(385, result)
    }

    @Test
    fun `square of sum of first 10 natural numbers equals 3025`() {
        val sum = (1..10).sum()
        assertEquals(3025, sum * sum)
    }

    @Test
    fun `difference for first 10 natural numbers equals 2640`() {
        val sumOfSquares = (1..10).sumOf { it * it }
        val sum = (1..10).sum()
        val squareOfSum = sum * sum
        assertEquals(2640, squareOfSum - sumOfSquares)
    }

    @Test
    fun `difference for first 100 natural numbers equals 25164150`() {
        val sumOfSquares = (1..100).sumOf { it * it }
        val sum = (1..100).sum()
        val squareOfSum = sum * sum
        assertEquals(25164150, squareOfSum - sumOfSquares)
    }
}

class P07Test {
    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        if (n == 2) return true
        if (n % 2 == 0) return false
        var i = 3
        while (i * i <= n) {
            if (n % i == 0) return false
            i += 2
        }
        return true
    }

    private fun nthPrime(n: Int): Int {
        var count = 0
        var candidate = 2
        while (count < n) {
            if (isPrime(candidate)) count++
            if (count < n) candidate++
        }
        return candidate
    }

    @Test
    fun `first prime is 2`() {
        assertEquals(2, nthPrime(1))
    }

    @Test
    fun `6th prime is 13`() {
        assertEquals(13, nthPrime(6))
    }

    @Test
    fun `10001st prime is 104743`() {
        assertEquals(104743, nthPrime(10001))
    }
}

class P08Test {
    @Test
    fun `product of single digit equals the digit`() {
        assertEquals(5, "5".map { it.digitToInt() }.fold(1) { acc, d -> acc * d })
    }

    @Test
    fun `product of 1 2 3 equals 6`() {
        assertEquals(6, "123".map { it.digitToInt() }.fold(1) { acc, d -> acc * d })
    }
}

class P09Test {
    @Test
    fun `3 4 5 is a pythagorean triplet`() {
        val a = 3
        val b = 4
        val c = 5
        assertEquals(c * c, a * a + b * b)
    }

    @Test
    fun `product of pythagorean triplet with sum 12 equals 60`() {
        var result = 0
        outer@ for (a in 1..12) {
            for (b in a..12) {
                val c = 12 - a - b
                if (c > 0 && a * a + b * b == c * c) {
                    result = a * b * c
                    break@outer
                }
            }
        }
        assertEquals(60, result)
    }

    @Test
    fun `product of pythagorean triplet with sum 1000 equals 31875000`() {
        var result = 0
        outer@ for (a in 1..1000) {
            for (b in a..1000) {
                val c = 1000 - a - b
                if (c > 0 && a * a + b * b == c * c) {
                    result = a * b * c
                    break@outer
                }
            }
        }
        assertEquals(31875000, result)
    }
}

class P10Test {
    private fun isPrime(n: Long): Boolean {
        if (n < 2L) return false
        if (n == 2L) return true
        if (n % 2L == 0L) return false
        var i = 3L
        while (i * i <= n) {
            if (n % i == 0L) return false
            i += 2
        }
        return true
    }

    @Test
    fun `sum of primes below 10 equals 17`() {
        val result = (2L until 10L).filter { isPrime(it) }.sum()
        assertEquals(17L, result)
    }
}

class P12Test {
    private fun countDivisors(n: Long): Int {
        var count = 0
        var i = 1L
        while (i * i <= n) {
            if (n % i == 0L) {
                count += if (i * i == n) 1 else 2
            }
            i++
        }
        return count
    }

    @Test
    fun `countDivisors of 1 equals 1`() {
        assertEquals(1, countDivisors(1))
    }

    @Test
    fun `countDivisors of 3 equals 2`() {
        assertEquals(2, countDivisors(3))
    }

    @Test
    fun `countDivisors of 6 equals 4`() {
        assertEquals(4, countDivisors(6))
    }

    @Test
    fun `countDivisors of 28 equals 6`() {
        assertEquals(6, countDivisors(28))
    }

    @Test
    fun `first triangle number with over 5 divisors is 28`() {
        var n = 1L
        var triangle = 1L
        while (countDivisors(triangle) <= 5) {
            n++
            triangle += n
        }
        assertEquals(28L, triangle)
    }
}

class P13Test {
    @Test
    fun `sum of two large numbers works correctly`() {
        val num1 = BigInteger("37107287533902102798797998220837590246510135740250")
        val num2 = BigInteger("46376937677490009712648124896970078050417018260538")
        val sum = num1 + num2
        assertTrue(sum.toString().startsWith("83484225211392112511446123117807668296927154000788"))
    }
}

class P14Test {
    private fun collatzLength(start: Long): Long {
        var n = start
        var length = 0L
        while (n != 1L) {
            n = if (n % 2L == 0L) n / 2 else 3 * n + 1
            length++
        }
        return length + 1
    }

    @Test
    fun `collatz sequence for 13 has length 10`() {
        assertEquals(10L, collatzLength(13))
    }

    @Test
    fun `collatz sequence for 1 has length 1`() {
        assertEquals(1L, collatzLength(1))
    }
}

class P15Test {
    private fun pathsInGrid(size: Int): Long {
        val dp = Array(size + 1) { LongArray(size + 1) }
        for (i in 0..size) {
            dp[i][0] = 1L
            dp[0][i] = 1L
        }
        for (i in 1..size) {
            for (j in 1..size) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
            }
        }
        return dp[size][size]
    }

    @Test
    fun `paths in 2x2 grid equals 6`() {
        assertEquals(6L, pathsInGrid(2))
    }

    @Test
    fun `paths in 20x20 grid equals 137846528820`() {
        assertEquals(137846528820L, pathsInGrid(20))
    }
}

class P16Test {
    @Test
    fun `sum of digits of 2^15 equals 26`() {
        val number = BigInteger.TWO.pow(15)
        val digitSum = number.toString().sumOf { it.digitToInt() }
        assertEquals(26, digitSum)
    }

    @Test
    fun `sum of digits of 2^1000 equals 1366`() {
        val number = BigInteger.TWO.pow(1000)
        val digitSum = number.toString().sumOf { it.digitToInt() }
        assertEquals(1366, digitSum)
    }
}

class P20Test {
    private fun factorial(n: Int): BigInteger {
        return (1..n).fold(BigInteger.ONE) { acc, i -> acc.multiply(BigInteger.valueOf(i.toLong())) }
    }

    @Test
    fun `factorial of 5 equals 120`() {
        assertEquals(BigInteger.valueOf(120), factorial(5))
    }

    @Test
    fun `sum of digits in 10! equals 27`() {
        val result = factorial(10).toString().sumOf { it.digitToInt() }
        assertEquals(27, result)
    }

    @Test
    fun `sum of digits in 100! equals 648`() {
        val result = factorial(100).toString().sumOf { it.digitToInt() }
        assertEquals(648, result)
    }
}

class P25Test {
    private fun firstFibWith1000Digits(): Int {
        var a = BigInteger.ONE
        var b = BigInteger.ONE
        var index = 2
        while (b.toString().length < 1000) {
            val temp = a + b
            a = b
            b = temp
            index++
        }
        return index
    }

    @Test
    fun `12th fibonacci number has 3 digits`() {
        var a = BigInteger.ONE
        var b = BigInteger.ONE
        repeat(10) {
            val temp = a + b
            a = b
            b = temp
        }
        assertEquals(3, b.toString().length)
    }
}
