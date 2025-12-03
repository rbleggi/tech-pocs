package com.rbleggi.ninetynine

object P02 {
    tailrec fun <A> penultimate(items: List<A>): A {
        println("Recursion step: $items")
        return when {
            items.size < 2 -> throw NoSuchElementException("penultimate of list with less than two elements")
            items.size == 2 -> items[0]
            else -> penultimate(items.drop(1))
        }
    }
}

fun mainP02() {
    println("Find the last but one element of a list.")
    val numbers = listOf(1, 1, 2, 3, 5, 8)
    println("The list is: $numbers")
    val penultimateElement = P02.penultimate(numbers)
    println("The penultimate element is: $penultimateElement")
}
