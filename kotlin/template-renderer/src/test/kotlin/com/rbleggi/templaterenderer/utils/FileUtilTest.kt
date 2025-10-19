package com.rbleggi.templaterenderer.utils

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class FileUtilTest {

    @Test
    fun `should write text content to a file`() {
        val tempFile = File.createTempFile("test_output", ".txt")
        try {
            FileUtil.saveToFile(tempFile.absolutePath, "test content.".toByteArray())
            assertTrue(tempFile.exists(), "File should be created")
            assertTrue(tempFile.readText() == "test content.", "File content should match")
        } finally {
            tempFile.delete()
        }
    }

}