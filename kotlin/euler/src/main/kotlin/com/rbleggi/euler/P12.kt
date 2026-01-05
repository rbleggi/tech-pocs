package com.rbleggi.euler

fun mainP12() {
    fun countDivisors(n: Long): Int {
        var num = n
        var count = 1
        var p = 2L
        while (p * p <= num) {
            var exp = 0
            while (num % p == 0L) {
                num /= p
                exp += 1
            }
            if (exp > 0) count *= (exp + 1)
            p = if (p == 2L) 3 else p + 2
        }
        if (num > 1) count *= 2
        return count
    }

    fun firstTriangleWithDivisors(limit: Int): Long {
        var n = 1L
        var triangle = 1L
        while (countDivisors(triangle) <= limit) {
            n += 1
            triangle += n
        }
        return triangle
    }

    println("The first triangle number with over 500 divisors is: ${firstTriangleWithDivisors(500)}")
}
