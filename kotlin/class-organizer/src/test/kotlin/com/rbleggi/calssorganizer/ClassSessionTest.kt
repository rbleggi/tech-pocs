package com.rbleggi.calssorganizer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassSessionTest {
    @Test
    fun `creates session with correct properties`() {
        val teacher = Teacher("Alice")
        val start = LocalDateTime.of(2025, 1, 1, 9, 0)
        val end = LocalDateTime.of(2025, 1, 1, 10, 0)
        val session = ClassSession("1", "Math", start, end, teacher)
        assertEquals("1", session.id)
        assertEquals("Math", session.subject)
    }
}
