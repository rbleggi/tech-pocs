package com.rbleggi.euler

fun mainP24() {
    val digits = (0..9).toList()
    var k = 1000000 - 1
    var available = digits.toMutableList()
    val result = mutableListOf<Int>()

    for (i in 9 downTo 0) {
        val f = (1..i).fold(1) { acc, v -> acc * v }
        val idx = if (f == 0) 0 else k / f
        result.add(available[idx])
        available.removeAt(idx)
        k %= f
    }

    println("The millionth lexicographic permutation is: ${result.joinToString("")}")
}
