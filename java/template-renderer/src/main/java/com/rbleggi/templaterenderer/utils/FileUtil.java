package com.rbleggi.templaterenderer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static void saveToFile(String filename, byte[] content) {
        var file = new File(filename);
        try (var outputStream = new FileOutputStream(file)) {
            outputStream.write(content);
            System.out.println("File saved to: " + filename + "\n");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}
