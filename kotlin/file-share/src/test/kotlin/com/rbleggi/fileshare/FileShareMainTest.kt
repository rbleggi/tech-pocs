package com.rbleggi.fileshare

import kotlin.test.*

class FileShareMainTest {
    @Test
    fun testUploadAndList() {
        val system = FileShareSystem(FileStorageFactory.create("encrypted"))
        val file = File("test.txt", "abc")
        system.upload(file)
        assertTrue(system.list().any { it.name == "test.txt" })
    }

    @Test
    fun testDownload() {
        val system = FileShareSystem(FileStorageFactory.create("encrypted"))
        val file = File("test.txt", "abc")
        system.upload(file)
        val downloaded = system.download("test.txt")
        assertEquals("abc", downloaded?.content)
    }

    @Test
    fun testDelete() {
        val system = FileShareSystem(FileStorageFactory.create("encrypted"))
        val file = File("test.txt", "abc")
        system.upload(file)
        system.delete("test.txt")
        assertNull(system.download("test.txt"))
    }

    @Test
    fun testSearch() {
        val system = FileShareSystem(FileStorageFactory.create("encrypted"))
        system.upload(File("report1.pdf"))
        system.upload(File("report2.pdf"))
        val results = system.search("report")
        assertEquals(2, results.size)
    }
}

