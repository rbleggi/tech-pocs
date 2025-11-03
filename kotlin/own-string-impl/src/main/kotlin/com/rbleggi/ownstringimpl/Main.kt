package com.rbleggi.ownstringimpl

class MyString(val value: String) {
    fun toArray(): CharArray {
        val arr = CharArray(value.length)
        for (i in value.indices) {
            arr[i] = value[i]
        }
        return arr
    }

    fun forEach(f: (Char) -> Unit) {
        for (i in value.indices) {
            f(value[i])
        }
    }

    fun reverse(): MyString {
        val arr = CharArray(value.length)
        for (i in value.indices) {
            arr[i] = value[value.length - 1 - i]
        }
        return MyString(String(arr))
    }

    fun iterator(): Iterator<Char> = object : Iterator<Char> {
        private var i = 0
        override fun hasNext(): Boolean = i < value.length
        override fun next(): Char = value[i++]
    }

    fun length(): Int = value.length

    fun charAt(idx: Int): Char = value[idx]

    override fun equals(other: Any?): Boolean {
        when (other) {
            is MyString -> {
                if (value.length != other.value.length) return false
                for (i in value.indices) {
                    if (value[i] != other.value[i]) return false
                }
                return true
            }
            is String -> {
                if (value.length != other.length) return false
                for (i in value.indices) {
                    if (value[i] != other[i]) return false
                }
                return true
            }
            else -> return false
        }
    }

    fun isEmpty(): Boolean = value.length == 0

    fun replace(oldChar: Char, newChar: Char): MyString {
        val arr = CharArray(value.length)
        for (i in value.indices) {
            arr[i] = if (value[i] == oldChar) newChar else value[i]
        }
        return MyString(String(arr))
    }

    fun substring(start: Int, end: Int): MyString {
        val arr = CharArray(end - start)
        for (i in start until end) {
            arr[i - start] = value[i]
        }
        return MyString(String(arr))
    }

    fun trim(): MyString {
        var start = 0
        var end = value.length - 1
        while (start <= end && (value[start] == ' ' || value[start] == '\t' || value[start] == '\n')) start++
        while (end >= start && (value[end] == ' ' || value[end] == '\t' || value[end] == '\n')) end--
        return if (start > end) MyString("") else substring(start, end + 1)
    }

    fun toJson(): String {
        val sb = StringBuilder()
        sb.append("{\"value\":\"")
        for (i in value.indices) {
            val c = value[i]
            if (c == '"' || c == '\\') sb.append('\\')
            sb.append(c)
        }
        sb.append("\"}")
        return sb.toString()
    }

    fun indexOf(c: Char): Int {
        for (i in value.indices) {
            if (value[i] == c) return i
        }
        return -1
    }

    override fun hashCode(): Int {
        var h = 0
        for (i in value.indices) {
            h = 31 * h + value[i].code  // 31 (efficient prime)
        }
        return h
    }

    override fun toString(): String = value

    companion object {
        fun create(s: String): MyString = MyString(s)
    }
}

fun main() {
    val s = MyString("  Hello, Kotlin!  ")
    println("toArray: ${s.toArray().joinToString(",")}")
    print("forEach: "); s.forEach { print(it) }; println()
    println("reverse: ${s.reverse()}")
    println("iterator: ${s.iterator().asSequence().joinToString("")}")
    println("length: ${s.length()}")
    println("charAt(1): ${s.charAt(1)}")
    println("equals: ${s.equals(MyString("  Hello, Kotlin!  "))}")
    println("isEmpty: ${s.isEmpty()}")
    println("replace: ${s.replace('l', 'x')}")
    println("substring(2,7): ${s.substring(2, 7)}")
    println("trim: ${s.trim()}")
    println("toJson: ${s.toJson()}")
    println("indexOf('K'): ${s.indexOf('K')}")
    println("hashCode: ${s.hashCode()}")
}
