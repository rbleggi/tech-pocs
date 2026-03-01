package com.rbleggi.euler

fun mainP18() {
    val triangle = listOf(
        listOf(75),
        listOf(95, 64),
        listOf(17, 47, 82),
        listOf(18, 35, 87, 10),
        listOf(20, 4, 82, 47, 65),
        listOf(19, 1, 23, 75, 3, 34),
        listOf(88, 2, 77, 73, 7, 63, 67),
        listOf(99, 65, 4, 28, 6, 16, 70, 92),
        listOf(41, 41, 26, 56, 83, 40, 80, 70, 33),
        listOf(41, 48, 72, 33, 47, 32, 37, 16, 94, 29),
        listOf(53, 71, 44, 65, 25, 43, 91, 52, 97, 51, 14),
        listOf(70, 11, 33, 28, 77, 73, 17, 78, 39, 68, 17, 57),
        listOf(91, 71, 52, 38, 17, 14, 91, 43, 58, 50, 27, 29, 48),
        listOf(63, 66, 4, 68, 89, 53, 67, 30, 73, 16, 69, 87, 40, 31),
        listOf(4, 62, 98, 27, 23, 9, 70, 98, 73, 93, 38, 53, 60, 4, 23)
    )

    val maxTotal = triangle.reversed().reduce { lower, upper ->
        upper.mapIndexed { idx, value ->
            value + maxOf(lower[idx], lower[idx + 1])
        }
    }.first()

    println(maxTotal)
}
