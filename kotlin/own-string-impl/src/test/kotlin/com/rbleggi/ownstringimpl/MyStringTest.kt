package com.rbleggi.ownstringimpl

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MyStringTest {
    @Test
    fun testLength() {
        assertEquals(5, MyString("hello").length())
        assertEquals(0, MyString("").length())
    }

    @Test
    fun testReverse() {
        assertEquals("olleh", MyString("hello").reverse().toString())
    }

    @Test
    fun testTrim() {
        assertEquals("hello", MyString("  hello  ").trim().toString())
    }

    @Test
    fun testIsEmpty() {
        assertTrue(MyString("").isEmpty())
        assertFalse(MyString("hello").isEmpty())
    }

    @Test
    fun testIndexOf() {
        assertEquals(1, MyString("hello").indexOf('e'))
        assertEquals(-1, MyString("hello").indexOf('x'))
    }

    @Test
    fun testToArray() {
        val arr = MyString("abc").toArray()
        assertEquals(3, arr.size)
        assertEquals('a', arr[0])
        assertEquals('b', arr[1])
        assertEquals('c', arr[2])
    }

    @Test
    fun testCharAt() {
        assertEquals('h', MyString("hello").charAt(0))
        assertEquals('e', MyString("hello").charAt(1))
    }

    @Test
    fun testEquals() {
        assertTrue(MyString("hello").equals(MyString("hello")))
        assertTrue(MyString("hello").equals("hello"))
        assertFalse(MyString("hello").equals(MyString("world")))
        assertFalse(MyString("hello").equals("world"))
    }

    @Test
    fun testReplace() {
        assertEquals("hxllo", MyString("hello").replace('e', 'x').toString())
    }

    @Test
    fun testSubstring() {
        assertEquals("ell", MyString("hello").substring(1, 4).toString())
    }

    @Test
    fun testToJson() {
        assertEquals("""{"value":"hello"}""", MyString("hello").toJson())
    }
}
