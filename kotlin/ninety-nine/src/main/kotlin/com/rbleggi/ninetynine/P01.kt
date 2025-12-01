package com.rbleggi.ninetynine

object P01 {
    tailrec fun <A> last(items: List<A>): A {
        println("Recursion step: $items")
        return when {
            items.isEmpty() -> throw NoSuchElementException("last of empty list")
            items.size == 1 -> items[0]
            else -> last(items.drop(1))
        }
    }
}

fun mainP01() {
    println("Find the last element of a list.")
    val numbers = listOf(1, 1, 2, 3, 5, 8)
    println("The list is: $numbers")
    val lastElement = P01.last(numbers)
    println("The last element is: $lastElement")
}
