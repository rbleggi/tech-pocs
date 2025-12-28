package com.rbleggi.euler

fun mainP04() {
    fun isPalindrome(n: Int): Boolean {
        val s = n.toString()
        return s == s.reversed()
    }

    var maxPalindrome = 0
    var factor1 = 0
    var factor2 = 0

    for (i in 999 downTo 100) {
        for (j in i downTo 100) {
            val product = i * j
            if (isPalindrome(product) && product > maxPalindrome) {
                maxPalindrome = product
                factor1 = i
                factor2 = j
            }
        }
    }

    println("Largest palindrome made from the product of two 3-digit numbers: $maxPalindrome = $factor1 x $factor2")
}
