package com.rbleggi.euler

fun mainP22() {
    val resource = object {}.javaClass.getResourceAsStream("/names.txt")
    val names = resource?.bufferedReader()?.readText()?.replace("\"", "")?.split(",")?.sorted() ?: emptyList()

    fun nameValue(name: String): Int = name.sumOf { it - 'A' + 1 }

    val totalScore = names.mapIndexed { idx, name ->
        nameValue(name) * (idx + 1)
    }.sum()

    println(totalScore)
}
