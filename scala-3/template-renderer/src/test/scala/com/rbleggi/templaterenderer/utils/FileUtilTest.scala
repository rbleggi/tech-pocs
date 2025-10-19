package com.rbleggi.templaterenderer.utils

import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import scala.io.Source

class FileUtilTest extends AnyFunSuite {
  test("should successfully write text content to a file") {
    val tempFile = File.createTempFile("test_output", ".txt")
    val testContent = "This is a test content.".getBytes("UTF-8")

    FileUtil.saveToFile(tempFile.getAbsolutePath, testContent)

    assert(tempFile.exists(), "File should be created")

    val fileContent = Source.fromFile(tempFile).mkString
    assert(fileContent == "This is a test content.", "File content should match expected output")

    tempFile.delete()
  }
}
