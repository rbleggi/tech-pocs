package com.rbleggi.euler

fun mainP09() {
    for (a in 1..998) {
        for (b in a + 1..999) {
            val c = 1000 - a - b
            if (a * a + b * b == c * c) {
                println("The Pythagorean triplet is: a=$a, b=$b, c=$c")
                println("The product abc is: ${a * b * c}")
                return
            }
        }
    }
}
