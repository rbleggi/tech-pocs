package com.rbleggi.euler

fun mainP02() {
    fun generateFibs() = sequence {
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

    val fibsUpTo4M = generateFibs().takeWhile { it <= 4000000 }
    val evenSum = fibsUpTo4M.filter { it % 2 == 0 }.sum()
    println("Sum of even Fibonacci numbers not exceeding 4 million: $evenSum")
}
