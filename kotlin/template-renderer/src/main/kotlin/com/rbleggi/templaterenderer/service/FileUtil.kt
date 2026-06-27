package com.rbleggi.templaterenderer.service

import java.io.File

object FileUtil {
    fun saveToFile(filename: String, content: ByteArray) {
        File(filename).outputStream().use { it.write(content) }
        println("File saved to: $filename")
    }
}
