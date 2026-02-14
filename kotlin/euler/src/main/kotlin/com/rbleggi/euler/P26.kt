package com.rbleggi.euler

fun mainP26() {
    fun recurringCycleLength(denominator: Int): Int {
        var remainder = 1
        var position = 0
        val seen = mutableMapOf<Int, Int>()
        while (remainder != 0 && !seen.contains(remainder)) {
            seen[remainder] = position
            remainder = (remainder * 10) % denominator
            position += 1
        }
        return if (remainder == 0) 0 else position - seen[remainder]!!
    }

    var maxLength = 0
    var resultD = 0
    for (d in 2 until 1000) {
        val length = recurringCycleLength(d)
        if (length > maxLength) {
            maxLength = length
            resultD = d
        }
    }
    println("The value of d < 1000 with the longest recurring cycle is $resultD (cycle length: $maxLength)")
}
