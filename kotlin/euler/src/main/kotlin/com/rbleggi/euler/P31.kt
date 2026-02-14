package com.rbleggi.euler

fun mainP31() {
    val coins = listOf(1, 2, 5, 10, 20, 50, 100, 200)
    val target = 200
    val ways = IntArray(target + 1)
    ways[0] = 1

    for (coin in coins) {
        for (amount in coin..target) {
            ways[amount] += ways[amount - coin]
        }
    }

    println("Number of ways to make £2 using any number of coins: ${ways[target]}")
}
