package com.rbleggi.euler

fun mainP17() {
    val units = mapOf(
        1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five",
        6 to "six", 7 to "seven", 8 to "eight", 9 to "nine"
    )
    val teens = mapOf(
        10 to "ten", 11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen",
        15 to "fifteen", 16 to "sixteen", 17 to "seventeen", 18 to "eighteen", 19 to "nineteen"
    )
    val tens = mapOf(
        20 to "twenty", 30 to "thirty", 40 to "forty", 50 to "fifty",
        60 to "sixty", 70 to "seventy", 80 to "eighty", 90 to "ninety"
    )

    fun numberToWords(n: Int): String {
        if (n == 1000) return "onethousand"
        if (n >= 100) {
            val hundred = n / 100
            val rest = n % 100
            val hundredWord = units[hundred]!! + "hundred"
            return if (rest == 0) hundredWord else hundredWord + "and" + numberToWords(rest)
        }
        if (n >= 20) {
            val ten = (n / 10) * 10
            val unit = n % 10
            return if (unit == 0) tens[ten]!! else tens[ten]!! + units[unit]!!
        }
        if (n >= 10) return teens[n]!!
        if (n >= 1) return units[n]!!
        return ""
    }

    val totalLetters = (1..1000).sumOf { numberToWords(it).length }
    println(totalLetters)
}
