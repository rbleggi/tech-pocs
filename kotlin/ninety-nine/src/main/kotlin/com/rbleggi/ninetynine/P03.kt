package com.rbleggi.ninetynine

object P03 {
    tailrec fun <A> nth(k: Int, items: List<A>): A {
        println("Recursion step: k = $k, $items")
        return when {
            items.isEmpty() -> throw NoSuchElementException("nth of empty list")
            k < 0 -> throw NoSuchElementException("negative index")
            k == 0 -> items[0]
            else -> nth(k - 1, items.drop(1))
        }
    }
}

fun mainP03() {
    println("Find the Kth element of a list")
    val numbers = listOf(1, 1, 2, 3, 5, 8)
    println("The list is: $numbers")
    val k = 4
    println("The index to find is: $k")
    val kthElement = P03.nth(k, numbers)
    println("The element at index $k is: $kthElement")
}
